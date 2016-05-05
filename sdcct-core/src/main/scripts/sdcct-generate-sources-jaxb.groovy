import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.util.ISO8601Utils
import com.github.sebhoss.warnings.CompilerWarnings
import com.sun.codemodel.CodeWriter
import com.sun.codemodel.JAnnotationUse
import com.sun.codemodel.JBlock
import com.sun.codemodel.JClass
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JExpr
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JMethod
import com.sun.codemodel.JMod
import com.sun.codemodel.JType
import com.sun.tools.ws.processor.generator.CustomExceptionGenerator
import com.sun.tools.ws.processor.generator.GeneratorBase
import com.sun.tools.ws.processor.generator.SeiGenerator
import com.sun.tools.ws.processor.modeler.wsdl.WSDLModeler
import com.sun.tools.ws.wscompile.WSCodeWriter
import com.sun.tools.ws.wsdl.parser.MetadataFinder
import com.sun.tools.ws.wsdl.parser.WSDLInternalizationLogic
import com.sun.tools.xjc.Language
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.Plugin
import com.sun.tools.xjc.generator.bean.DualObjectFactoryGenerator
import com.sun.tools.xjc.model.CClassInfo
import com.sun.tools.xjc.model.CPropertyInfo
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.ClassOutline
import com.sun.tools.xjc.outline.Outline
import com.sun.xml.ws.util.ServiceFinder
import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenCodeWriter
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenErrorReceiver
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenModelBuilder
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaCompiler
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenWsOptions
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.HierarchyCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.PackageCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.RuntimeCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.CompositeNameConverter
import gov.hhs.onc.sdcct.build.xml.jaxb.term.impl.FhirTermCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.type.impl.FhirTypeCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.type.impl.RfdTypeCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.utils.SdcctCodegenUtils
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.stream.Collectors
import javax.annotation.Generated
import javax.xml.bind.JAXBElement
import javax.xml.bind.annotation.XmlRootElement
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.map.MultiKeyMap
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.MojoExecutionException
import org.apache.tools.ant.types.FileSet

def final DEFAULT_XJC_ARGS = [
    "-Xdefault-value",
    "-Xsetters",
    "-Xannotate"
]

def errorReceiver = new CodegenErrorReceiver(log)
def xjcErrorReceiver = errorReceiver.forXjc()
def wsimportErrorReceiver = errorReceiver.forWsimport()
def bindingVars = this.binding.variables

def schemaFiles = SdcctBuildUtils.extractFiles(((FileSet) ant.fileset(dir: new File(project.properties.getProperty("project.build.schemaDirectory"))) {
    SdcctBuildUtils.tokenize(((String) bindingVars["schemaIncludes"])).each {
        ant.include(name: it)
    }

    SdcctBuildUtils.tokenize(((String) bindingVars["schemaExcludes"])).each {
        ant.exclude(name: it)
    }
}))

def bindingFiles = SdcctBuildUtils.extractFiles(((FileSet) ant.fileset(dir: new File(project.properties.getProperty("project.build.jaxbDirectory"))) {
    SdcctBuildUtils.tokenize(((String) bindingVars["bindingIncludes"])).each {
        ant.include(name: it)
    }

    SdcctBuildUtils.tokenize(((String) bindingVars["bindingExcludes"])).each {
        ant.exclude(name: it)
    }
}))

def outDir = new File(((String) bindingVars["outDir"]))
outDir.mkdirs()
project.addCompileSourceRoot(outDir.path)

def debug = BooleanUtils.toBoolean(((String) bindingVars["debug"]))
def enc = project.properties["project.build.sourceEncoding"]
def verbose = BooleanUtils.toBoolean(((String) bindingVars["verbose"]))
def wsimport = BooleanUtils.toBoolean(((String) bindingVars["wsimport"]))
def Map<String, String> pkgNames = SdcctBuildUtils.tokenizeMap(((String) bindingVars["pkgNames"]))
def Map<String, String> implPkgNames = ((Map<String, String>) pkgNames.collectEntries{ [ it.key, (it.value + ClassUtils.PACKAGE_SEPARATOR +
    SdcctClassUtils.IMPL_PKG_NAME) ] })
def basePkgName = pkgNames.values().iterator().next()
def baseImplPkgName = implPkgNames.values().iterator().next()
def generatorName = "sdcct-generate-sources-jaxb"
def generationTime = ISO8601Utils.format(new Date())
def generationComments = ("JAXB RI v" + Options.getBuildID())
def CodegenSchemaContext schemaContext = new CodegenSchemaContext()
def Options opts = new Options()
def CodegenWsOptions wsOpts = null
def CodegenSchemaCompiler schemaCompiler = null
def List<String> args
def JCodeModel codeModel = new JCodeModel()
def Model model = null
def Outline outline
def CodeWriter codeWriter

if (wsimport) {
    schemaCompiler = (wsOpts = new CodegenWsOptions(opts, xjcErrorReceiver, schemaContext)).schemaCompiler
    wsOpts.codeModel = codeModel
    wsOpts.compatibilityMode = com.sun.tools.ws.wscompile.Options.EXTENSION
    wsOpts.debug = debug
    wsOpts.debugMode = debug
    wsOpts.encoding = enc
    wsOpts.keep = true
    wsOpts.nocompile = true
    wsOpts.sourceDir = outDir
    wsOpts.verbose = verbose

    schemaFiles.each{ wsOpts.addSchema(it) }

    SdcctBuildUtils.extractFiles(((FileSet) ant.fileset(dir: new File(project.properties.getProperty("project.build.wsdlDirectory"))) {
        SdcctBuildUtils.tokenize(((String) bindingVars["wsdlIncludes"])).each {
            ant.include(name: it)
        }

        SdcctBuildUtils.tokenize(((String) bindingVars["wsdlExcludes"])).each {
            ant.exclude(name: it)
        }
    })).each{ wsOpts.addWSDL(it) }
    
    bindingFiles.each{ wsOpts.addBindings(it.path) }

    CollectionUtils.addAll((args = new ArrayList<String>(DEFAULT_XJC_ARGS)), SdcctBuildUtils.tokenize(((String) bindingVars["args"])))
    args = args.collect{ ("-B" + it) }
} else {
    schemaFiles.each{ opts.addGrammar(it) }
    
    bindingFiles.each{ opts.addBindFile(it) }

    CollectionUtils.addAll((args = new ArrayList<String>(DEFAULT_XJC_ARGS)), SdcctBuildUtils.tokenize(((String) bindingVars["args"])))
}
    
opts.automaticNameConflictResolution = true
opts.compatibilityMode = Options.EXTENSION
opts.contentForWildcard = true
opts.debugMode = debug
opts.encoding = enc
opts.noFileHeader = true
opts.schemaLanguage = Language.XMLSCHEMA
opts.targetDir = outDir
opts.verbose = verbose

opts.setNameConverter(new CompositeNameConverter(schemaContext), null)

HierarchyCodegenPlugin hierarchyPlugin = new HierarchyCodegenPlugin(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext)
opts.allPlugins.add(hierarchyPlugin)
opts.activePlugins.add(hierarchyPlugin)

PackageCodegenPlugin pkgPlugin = new PackageCodegenPlugin(log, project, bindingVars, pkgNames, implPkgNames, basePkgName, baseImplPkgName, codeModel,
    schemaContext)
opts.allPlugins.add(pkgPlugin)
opts.activePlugins.add(pkgPlugin)

RuntimeCodegenPlugin runtimePlugin = new RuntimeCodegenPlugin(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext)
opts.allPlugins.add(runtimePlugin)
opts.activePlugins.add(runtimePlugin)

opts.allPlugins.addAll([
    new FhirTermCodegenPlugin(log, project, bindingVars, codeModel, schemaContext),
    new FhirTypeCodegenPlugin(log, project, bindingVars, codeModel, schemaContext),
    new RfdTypeCodegenPlugin(log, project, bindingVars, codeModel, schemaContext)
] as Plugin[])

try {
    if (wsimport) {
        wsOpts.parseArguments(args.toArray(new String[args.size()]))
        wsOpts.validate()
        wsOpts.parseBindings(wsimportErrorReceiver)
    } else {
        opts.parseArguments(args.toArray(new String[args.size()]))
    }
} catch (e) {
    throw new MojoExecutionException("Unable to parse arguments: ${StringUtils.join(args, StringUtils.SPACE)}", e)
}

log.info("Parsed arguments: ${StringUtils.join(args, StringUtils.SPACE)}")

try {
    if (wsimport) {
        def metadataFinder = new MetadataFinder(new WSDLInternalizationLogic(), wsOpts, wsimportErrorReceiver)
        metadataFinder.parseWSDL()

        def wsdlModel = new WSDLModeler(wsOpts, wsimportErrorReceiver, metadataFinder).buildModel()

        model = schemaCompiler.model
        outline = schemaCompiler.outline

        CustomExceptionGenerator.generate(wsdlModel, wsOpts, wsimportErrorReceiver)
        SeiGenerator.generate(wsdlModel, wsOpts, wsimportErrorReceiver)

        for (GeneratorBase genBase : ServiceFinder.find(GeneratorBase)) {
            genBase.init(wsdlModel, wsOpts, wsimportErrorReceiver)
            genBase.doGeneration()
        }

        for (com.sun.tools.ws.wscompile.Plugin plugin : wsOpts.activePlugins) {
            plugin.run(wsdlModel, wsOpts, wsimportErrorReceiver)
        }
    } else {
        CodegenModelBuilder modelBuilder = new CodegenModelBuilder(opts, xjcErrorReceiver, codeModel, schemaContext)
        
        if (((schemaContext = modelBuilder.buildSchemas()) == null) || ((model = modelBuilder.buildModel()) == null) ||
            ((outline = modelBuilder.buildOutline()) == null)) {
            throw new MojoExecutionException("Unable to generate code model for class(es).")
        }
    }
} catch (MojoExecutionException e) {
    throw e
} catch (e) {
    throw new MojoExecutionException("Unable to generate code outline for class(es).", e)
}

if (wsimport) {
    codeModel = wsOpts.codeModel
    codeWriter = new WSCodeWriter(outDir, wsOpts)
} else {
    codeWriter = opts.createCodeWriter()
}

codeWriter = new CodegenCodeWriter(codeWriter)

def beans = model.beans()
def numBeans = beans.size()

log.info("Generated code outline for ${numBeans} class(es).")

def compilerWarningsClassName = CompilerWarnings.name
def compilerWarningsClassModel = codeModel.directClass(compilerWarningsClassName)
def rawTypesFieldName = "RAWTYPES"
def uncheckedFieldName = "UNCHECKED"
def rawTypesStaticRef = compilerWarningsClassModel.staticRef(rawTypesFieldName)
def uncheckedStaticRef = compilerWarningsClassModel.staticRef(uncheckedFieldName)
def abstractImplClassNames = new HashSet<String>()
def objFactoryGen
def CClassInfo classModel
def JDefinedClass classRef
def JDefinedClass implClass
def String className
def String implClassName
def List<JAnnotationUse> classAnnos
def JAnnotationUse implAnnoModel
def Map<String, JFieldVar> fields
def Map<String, JMethod> classMethods
def Map<String, JMethod> implClassMethods
def Map<String, CPropertyInfo> props
def MultiKeyMap<String, JMethod> propSetters = new MultiKeyMap<>()
def MultiKeyMap<String, JMethod> propAdders = new MultiKeyMap<>()
def String publicPropName
def String propGetterName
def boolean propIsGetter
def JMethod propGetter
def JMethod propSetter
def propClass
def String privatePropName
def JMethod implPropGetter
def JMethod implPropSetter
def String propSetterName
def implPropSetterBody
def String propAdderName
def JClass propItemClass
def JMethod propAdder
def JMethod implPropAdder
def JBlock implPropAdderBody
def String hasPropMethodName
def JClass superImplClass
def String superClassName
def String superImplClassName
def ClassOutline superClassOutline
def CClassInfo superClassModel
def JMethod superPropAccessor
def JType superPropAccessorParamType

def Field classModelAnnosField = JDefinedClass.getDeclaredField("annotations")
classModelAnnosField.accessible = true

def Field methodModelAnnosField = JMethod.getDeclaredField("annotations")
methodModelAnnosField.accessible = true

def Method annoModelAddValueMethod = JAnnotationUse.declaredMethods.find{ it.name == "addValue" }
annoModelAddValueMethod.accessible = true

outline.allPackageContexts.each{
    [
        (objFactoryGen = ((DualObjectFactoryGenerator) it.objectFactoryGenerator())).publicOFG,
        objFactoryGen.privateOFG
    ].each{ it.objectFactory.annotate(SuppressWarnings).paramArray(SdcctCodegenUtils.VALUE_MEMBER_NAME).param(rawTypesStaticRef).param(uncheckedStaticRef) }
    
    it.classes?.each{
        (implClass = it.implClass).annotate(Generated).param(SdcctCodegenUtils.VALUE_MEMBER_NAME, generatorName).param(SdcctCodegenUtils.DATE_MEMBER_NAME,
            generationTime).param(SdcctCodegenUtils.COMMENTS_FIELD_NAME, generationComments)
        
        if (((classRef = it.ref) == null) || ((classModel = it.target) == null)) {
            return
        }
        
        className = classRef.fullName()
        implClassName = implClass.fullName()
        classAnnos = classModelAnnosField.get(classRef)
        
        classRef.annotations().findAll{ (it.annotationClass.fullName() == JsonTypeName.class.name) ||
            (it.annotationClass.fullName() == XmlRootElement.name) }?.each{
            implAnnoModel = implClass.annotate(it.annotationClass)
            
            it.annotationMembers.each{
                annoModelAddValueMethod.invoke(implAnnoModel, it.key, it.value)
            }
            
            classAnnos.remove(it)
        }
        
        classRef.annotate(Generated).param(SdcctCodegenUtils.VALUE_MEMBER_NAME, generatorName).param(SdcctCodegenUtils.DATE_MEMBER_NAME, generationTime)
            .param(SdcctCodegenUtils.COMMENTS_FIELD_NAME, generationComments)
        
        if (implClass.abstract) {
            abstractImplClassNames.add(implClass.name())
        }
        
        fields = implClass.fields()
        classMethods = classRef.methods().stream().collect(Collectors.toMap({ it.name() }, { it }, { classMethod1, classMethod2 -> classMethod2 }))
        implClassMethods = implClass.methods().stream().collect(Collectors.toMap({ it.name() }, { it },
            { implClassMethod1, implClassMethod2 -> implClassMethod2 }))
        props = classModel.properties.stream().collect(Collectors.toMap({ it.getName(true) }, { it }, { prop1, prop2 -> prop2 }))
        
        props.values().each{
            if (!implClassMethods.containsKey((propGetterName = (SdcctCodegenUtils.GETTER_METHOD_NAME_PREFIX + (publicPropName = it.getName(true))))) &&
                !(propIsGetter = implClassMethods.containsKey((propGetterName = (SdcctCodegenUtils.IS_GETTER_METHOD_NAME_PREFIX + publicPropName))))) {
                return;
            }
            
            propClass = (propGetter = classMethods[propGetterName]).type()
            privatePropName = it.getName(false)
            
            (implPropGetter = implClassMethods[propGetterName]).annotations().find{ it.annotationClass.fullName() == JsonProperty.class.name }.each{
                methodModelAnnosField.get(implPropGetter).remove(it)
                
                propGetter.annotations()
                
                methodModelAnnosField.get(propGetter).add(it)
            }
            
            if (propIsGetter) {
                [ propGetter, implPropGetter ].each{ it.name((SdcctCodegenUtils.GETTER_METHOD_NAME_PREFIX + publicPropName)) }
            }
            
            if (implClassMethods.containsKey((propSetterName = (SdcctCodegenUtils.SETTER_METHOD_NAME_PREFIX + publicPropName)))) {
                if (classMethods.containsKey(propSetterName)) {
                    (propSetter = classMethods[propSetterName]).type(classRef)
                } else {
                    (propSetter = classRef.method(JMod.NONE, classRef, propSetterName)).param(propClass, privatePropName)
                }
                
                (implPropSetter = implClassMethods[propSetterName]).type(classRef)
                implPropSetter.body()._return(JExpr._this())
                
                if (StringUtils.startsWith(implPropSetter?.params()[0].type().fullName(), JAXBElement.name)) {
                    implPropSetter.annotate(SuppressWarnings).paramArray(SdcctCodegenUtils.VALUE_MEMBER_NAME).param(uncheckedStaticRef)
                } else {
                    propSetters.put(className, publicPropName, propSetter)
                    propSetters.put(implClassName, publicPropName, implPropSetter)
                }
            }
            
            if (it.collection) {
                (propAdder = classRef.method(JMod.NONE, classRef, (propAdderName = (SdcctCodegenUtils.ADD_METHOD_NAME + publicPropName))))
                    .varParam((propItemClass = ((JClass) propClass).getTypeParameters()[0]), privatePropName);
                
                (implPropAdder = implClass.method(JMod.PUBLIC, classRef, propAdderName)).varParam(propItemClass, privatePropName)
                
                (implPropAdderBody = implPropAdder.body()).staticInvoke(codeModel.directClass(CollectionUtils.name), SdcctCodegenUtils.ADD_ALL_METHOD_NAME)
                    .arg(JExpr._this().invoke(implPropGetter)).arg(JExpr.direct(privatePropName))
                implPropAdderBody._return(JExpr._this())
                
                if (StringUtils.startsWith(propItemClass.fullName(), JAXBElement.name)) {
                    propAdder.annotate(SuppressWarnings).paramArray(SdcctCodegenUtils.VALUE_MEMBER_NAME).param(uncheckedStaticRef)
                    implPropAdder.annotate(SuppressWarnings).paramArray(SdcctCodegenUtils.VALUE_MEMBER_NAME).param(uncheckedStaticRef)
                } else {
                    propAdders.put(className, publicPropName, propAdder)
                    propAdders.put(implClassName, publicPropName, implPropAdder)
                }
            }
            
            classRef.method(JMod.NONE, codeModel.BOOLEAN, (hasPropMethodName = (SdcctCodegenUtils.HAS_METHOD_NAME_PREFIX + publicPropName)));
            
            implClass.method(JMod.PUBLIC, codeModel.BOOLEAN, hasPropMethodName).body()._return(JExpr._this().ref(privatePropName)
                .ne((propClass.primitive ? ((propClass == codeModel.BOOLEAN) ? JExpr.lit(false) : JExpr.lit(0)) : JExpr._null())))
        }
    }
    
    it.classes?.each{
        if (((classModel = it.target) == null) || ((classRef = it.ref) == null) || ((implClass = it.implClass) == null)) {
            return
        }
        
        className = classRef.fullName()
        implClassName = Optional.ofNullable(it.target.userSpecifiedImplClass).orElse(implClass.fullName())
        superImplClass = implClass
        
        while ((superImplClassName = (superImplClass = superImplClass._extends()).fullName()) != Object.name) {
            if ((superClassName = (superClassOutline = outline.classes.find{ (it.implClass.fullName() == superImplClassName) })?.ref?.fullName()) == null) {
                continue
            }
            
            (superClassModel = superClassOutline.target).properties.each{
                publicPropName = it.getName(true)
                privatePropName = it.getName(false)

                if (propSetters.containsKey(superClassName, publicPropName)) {
                    superPropAccessor = propSetters.get(superClassName, publicPropName)
                    propSetterName = (SdcctCodegenUtils.SETTER_METHOD_NAME_PREFIX + publicPropName)

                    if (propSetters.containsKey(className, publicPropName)) {
                        if ((propSetter = propSetters.get(className, publicPropName)).type().fullName() != className) {
                            propSetter.type(classRef)

                            propSetters.get(implClassName, publicPropName).type(classRef)
                        }
                    } else {
                        (propSetter = classRef.method(JMod.NONE, classRef, propSetterName))
                            .param(JMod.NONE, (superPropAccessorParamType = superPropAccessor.params()[0].type()), privatePropName)
                        propSetter.annotate(Override)

                        (implPropSetter = implClass.method(JMod.PUBLIC, classRef, propSetterName))
                            .param(JMod.NONE, superPropAccessorParamType, privatePropName)
                        implPropSetter.annotate(Override)

                        (implPropSetterBody = implPropSetter.body())._return(JExpr.cast(classRef, JExpr._super().invoke(superPropAccessor)
                            .arg(JExpr.direct(privatePropName))))
                    }
                }

                if (it.collection && propAdders.containsKey(superClassName, publicPropName)) {
                    superPropAccessor = propAdders.get(superClassName, publicPropName)
                    propAdderName = (SdcctCodegenUtils.ADD_METHOD_NAME + publicPropName)
                    
                    if (SdcctCodegenUtils.hasMethod(classRef, propAdderName)) {
                        if ((propAdder = propAdders.get(className, publicPropName)).type().fullName() != className) {
                            propAdder.type(classRef)

                            propAdders.get(implClassName, publicPropName).type(classRef)
                        }
                    } else {
                        (propAdder = classRef.method(JMod.NONE, classRef, propAdderName))
                            .varParam((superPropAccessorParamType = superPropAccessor.listVarParam().type().elementType()), privatePropName)
                        propAdder.annotate(Override)

                        (implPropAdder = implClass.method(JMod.PUBLIC, classRef, propAdderName))
                            .varParam(superPropAccessorParamType, privatePropName)
                        implPropAdder.annotate(Override)

                        (implPropAdderBody = implPropAdder.body())._return(JExpr.cast(classRef, JExpr._super().invoke(superPropAccessor)
                            .arg(JExpr.direct(privatePropName))))
                    }
                }
            }
        }
    }
}

outline.enums.each{
    it.clazz.annotate(Generated).param(SdcctCodegenUtils.VALUE_MEMBER_NAME, generatorName).param(SdcctCodegenUtils.DATE_MEMBER_NAME, generationTime)
        .param(SdcctCodegenUtils.COMMENTS_FIELD_NAME, generationComments)
}

try {
    codeModel.build(codeWriter)
} catch (e) {
    throw new MojoExecutionException("Unable to build code model for ${numBeans} class(es).", e)
}

log.info("Built code model for ${numBeans} class(es).")
