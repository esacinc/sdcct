import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.util.ISO8601Utils
import com.github.sebhoss.warnings.CompilerWarnings
import com.sun.codemodel.CodeWriter
import com.sun.codemodel.JAnnotationUse
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
import com.sun.tools.ws.wscompile.Plugin
import com.sun.tools.ws.wscompile.WSCodeWriter
import com.sun.tools.ws.wscompile.WsimportOptions
import com.sun.tools.ws.wsdl.parser.MetadataFinder
import com.sun.tools.ws.wsdl.parser.WSDLInternalizationLogic
import com.sun.tools.xjc.Language
import com.sun.tools.xjc.ModelLoader
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.generator.bean.DualObjectFactoryGenerator
import com.sun.tools.xjc.model.CClassInfo
import com.sun.tools.xjc.model.CPropertyInfo
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.ClassOutline
import com.sun.tools.xjc.outline.Outline
import com.sun.xml.ws.util.ServiceFinder
import gov.hhs.onc.sdcct.tools.codegen.impl.CodegenErrorReceiver
import gov.hhs.onc.sdcct.tools.codegen.impl.CodegenNameConverter
import gov.hhs.onc.sdcct.tools.utils.SdcctToolUtils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.stream.Collectors
import javax.annotation.Generated
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.annotation.XmlNs
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSchema
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.map.MultiKeyMap
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.MojoExecutionException
import org.eclipse.persistence.jaxb.JAXBContextFactory

def final JAVA_SRC_FILE_EXT = ".java"

def final IMPL_PKG_NAME_SUFFIX = ".impl"

def final ABSTRACT_CLASS_NAME_PREFIX = "Abstract"

def final IMPL_CLASS_NAME_SUFFIX = "Impl"

def final JAXB_CONTEXT_FACTORY_SIMPLE_CLASS_NAME = "JAXBContextFactory"

def final COMMENTS_FIELD_NAME = "comments"
def final DATE_MEMBER_NAME = "date"
def final VALUE_MEMBER_NAME = "value"

def final ADD_METHOD_NAME = "add"

def final GETTER_METHOD_NAME_PREFIX = "get"
def final HAS_METHOD_NAME_PREFIX = "has"
def final IS_METHOD_NAME_PREFIX = "is"
def final SETTER_METHOD_NAME_PREFIX = "set"

def final MOXY_JAXB_CONTEXT_FACTORY_JAXB_PROPS_CONTENT = (JAXBContext.JAXB_CONTEXT_FACTORY + "=" + JAXBContextFactory.name)

def final DEFAULT_XJC_ARGS = [
    "-Xdefault-value",
    "-Xsetters",
    "-Xannotate"
]

def errorReceiver = new CodegenErrorReceiver(log)
def xjcErrorReceiver = errorReceiver.forXjc()
def wsimportErrorReceiver = errorReceiver.forWsimport()
def bindingVars = this.binding.variables
def jaxbSrcDir = new File(project.properties.getProperty("project.build.jaxbSourceDirectory"))
def schemaSrcDir = new File(project.properties.getProperty("project.build.schemaSourceDirectory"))

def outDir = new File(outDir)
outDir.mkdirs()
project.addCompileSourceRoot(outDir.path)

def debug = BooleanUtils.toBoolean(((String) bindingVars["debug"]))
def enc = project.properties["project.build.sourceEncoding"]
def verbose = BooleanUtils.toBoolean(((String) bindingVars["verbose"]))
def wsimport = BooleanUtils.toBoolean(((String) bindingVars["wsimport"]))
def generatorName = "sdcct-generate-sources-jaxb"
def generationTime = ISO8601Utils.format(new Date())
def generationComments = ("JAXB RI v" + Options.getBuildID())
def wsimportOpts = null
def opts
def args
def JCodeModel codeModel
def Model model
def Outline outline
def CodeWriter codeWriter

if (wsimport) {
    opts = (wsimportOpts = new WsimportOptions()).schemaCompiler.options
    wsimportOpts.compatibilityMode = com.sun.tools.ws.wscompile.Options.EXTENSION
    wsimportOpts.debug = debug
    wsimportOpts.debugMode = debug
    wsimportOpts.encoding = enc
    wsimportOpts.keep = true
    wsimportOpts.nocompile = true
    wsimportOpts.sourceDir = outDir
    wsimportOpts.verbose = verbose

    (ant.fileset(dir: jaxbSrcDir) {
        SdcctToolUtils.tokenize(((String) bindingVars["bindingIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String) bindingVars["bindingExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        wsimportOpts.addBindings(it.file.path)
    }

    (ant.fileset(dir: schemaSrcDir) {
        SdcctToolUtils.tokenize(((String) bindingVars["schemaIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String) bindingVars["schemaExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        wsimportOpts.addSchema(it.file)
    }

    (ant.fileset(dir: new File(project.properties.getProperty("project.build.wsdlSourceDirectory"))) {
        SdcctToolUtils.tokenize(((String) bindingVars["wsdlIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String) bindingVars["wsdlExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        wsimportOpts.addWSDL(it.file)
    }

    CollectionUtils.addAll((args = DEFAULT_XJC_ARGS.collect(new ArrayList<String>(), { ("-B" + it) })), SdcctToolUtils.tokenize(((String) bindingVars["args"])))
} else {
    opts = new Options()
    
    (ant.fileset(dir: jaxbSrcDir) {
        SdcctToolUtils.tokenize(((String) bindingVars["bindingIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String) bindingVars["bindingExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        opts.addBindFile(it.file)
    }

    (ant.fileset(dir: schemaSrcDir) {
        SdcctToolUtils.tokenize(((String) bindingVars["schemaIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String) bindingVars["schemaExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        opts.addGrammar(it.file)
    }

    CollectionUtils.addAll((args = new ArrayList<String>(DEFAULT_XJC_ARGS)), SdcctToolUtils.tokenize(((String) bindingVars["args"])))
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

opts.setNameConverter(new CodegenNameConverter(), null)

try {
    if (wsimport) {
        wsimportOpts.parseArguments(args.toArray(new String[args.size()]))
        wsimportOpts.validate()
        wsimportOpts.parseBindings(wsimportErrorReceiver)
    } else {
        opts.parseArguments(args.toArray(new String[args.size()]))
    }
} catch (e) {
    throw new MojoExecutionException("Unable to parse arguments: ${StringUtils.join(args, " ")}", e)
}

log.info("Parsed arguments: ${StringUtils.join(args, " ")}")

def plugins = []

plugins.each{
    opts.activePlugins.add(it.forXjc())
}

try {
    if (wsimport) {
        plugins.each {
            wsimportOpts.activePlugins.add(it.forWsimport())
        }

        def metadataFinder = new MetadataFinder(new WSDLInternalizationLogic(), wsimportOpts, wsimportErrorReceiver)
        metadataFinder.parseWSDL()

        def wsdlModel = new WSDLModeler(wsimportOpts, wsimportErrorReceiver, metadataFinder).buildModel()
        def wsdlJaxbModel = wsdlModel.JAXBModel.s2JJAXBModel
        def wsdlJaxbModelClass = wsdlJaxbModel.class

        def wsdlJaxbModelModelField = wsdlJaxbModelClass.declaredFields.find { (it.name == "model") }
        wsdlJaxbModelModelField.accessible = true
        model = ((Model) wsdlJaxbModelModelField.get(wsdlJaxbModel))

        def wsdlJaxbModelOutlineField = wsdlJaxbModelClass.declaredFields.find { (it.name == "outline") }
        wsdlJaxbModelOutlineField.accessible = true
        outline = ((Outline)wsdlJaxbModelOutlineField.get(wsdlJaxbModel))

        CustomExceptionGenerator.generate(wsdlModel, wsimportOpts, wsimportErrorReceiver)
        SeiGenerator.generate(wsdlModel, wsimportOpts, wsimportErrorReceiver)

        for (GeneratorBase genBase : ServiceFinder.find(GeneratorBase)) {
            genBase.init(wsdlModel, wsimportOpts, wsimportErrorReceiver)
            genBase.doGeneration()
        }

        for (Plugin plugin : wsimportOpts.activePlugins) {
            plugin.run(wsdlModel, wsimportOpts, wsimportErrorReceiver)
        }
    } else {
        if ((model = ModelLoader.load(opts, (codeModel = new JCodeModel()), xjcErrorReceiver)) != null) {
            outline = model.generateCode(opts, xjcErrorReceiver)
        } else {
            throw new MojoExecutionException("Unable to generate code model for class(es).")
        }
    }
} catch (MojoExecutionException e) {
    throw e
} catch (e) {
    throw new MojoExecutionException("Unable to generate code outline for class(es).", e)
}

if (wsimport) {
    codeModel = wsimportOpts.codeModel
    codeWriter = new WSCodeWriter(outDir, wsimportOpts)
} else {
    codeWriter = opts.createCodeWriter()
}

def beans = model.beans()
def numBeans = beans.size()

log.info("Generated code outline for ${numBeans} class(es).")

def xmlNsPrefixes = SdcctToolUtils.tokenizeMap(((String) bindingVars["xmlNsPrefixes"]))
def xmlNsPrefixStaticRefs = xmlNsPrefixes.collectEntries(new LinkedHashMap<String, String>(xmlNsPrefixes.size()),
    { [ it.key, codeModel.directClass(ClassUtils.getPackageName(it.value)).staticRef(ClassUtils.getShortClassName(it.value)) ] })
def xmlNsUris = SdcctToolUtils.tokenizeMap(((String) bindingVars["xmlNsUris"]))
def xmlNsUriStaticRefs = xmlNsUris.collectEntries(new LinkedHashMap<String, String>(xmlNsUris.size()),
    { [ it.key, codeModel.directClass(ClassUtils.getPackageName(it.value)).staticRef(ClassUtils.getShortClassName(it.value)) ] })
def pkgNames = SdcctToolUtils.tokenizeMap(((String) bindingVars["pkgNames"]))
def implPkgNames = pkgNames.collectEntries{ [ it.key, (it.value + IMPL_PKG_NAME_SUFFIX) ] }

implPkgNames.each{
    def implPkg = codeModel._package(it.value)
    def xmlSchemaAnno = implPkg.annotations().find{ it.annotationClass.fullName() == XmlSchema.class.name }
    
    if (xmlSchemaAnno == null) {
        xmlSchemaAnno = implPkg.annotate(XmlSchema.class)
        xmlSchemaAnno.param("elementFormDefault", codeModel.directClass("javax.xml.bind.annotation.XmlNsForm").staticRef("QUALIFIED"))
    }
    
    xmlSchemaAnno.param("namespace", xmlNsUriStaticRefs[it.key]).paramArray("xmlns").annotate(XmlNs.class)
        .param("prefix", xmlNsPrefixStaticRefs[it.key]).param("namespaceURI", xmlNsUriStaticRefs[it.key])
}

def compilerWarningsClassName = CompilerWarnings.class.name
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
def propItemClass
def JMethod propAdder
def JMethod implPropAdder
def implPropAdderBody
def String hasPropMethodName
def JClass superImplClass
def String superClassName
def String superImplClassName
def ClassOutline superClassOutline
def CClassInfo superClassModel
def JMethod superPropAccessor
def JType superPropAccessorParamType

def Field classModelAnnosField = JDefinedClass.class.getDeclaredField("annotations")
classModelAnnosField.accessible = true

def Field methodModelAnnosField = JMethod.class.getDeclaredField("annotations")
methodModelAnnosField.accessible = true

def Method annoModelAddValueMethod = JAnnotationUse.class.declaredMethods.find{ it.name == "addValue" }
annoModelAddValueMethod.accessible = true

outline.allPackageContexts.each{
    [
        (objFactoryGen = ((DualObjectFactoryGenerator) it.objectFactoryGenerator())).publicOFG,
        objFactoryGen.privateOFG
    ].each{ it.objectFactory.annotate(SuppressWarnings.class).paramArray(VALUE_MEMBER_NAME).param(rawTypesStaticRef).param(uncheckedStaticRef) }
    
    it.classes?.each{
        (implClass = it.implClass).annotate(Generated).param(VALUE_MEMBER_NAME, generatorName).param(DATE_MEMBER_NAME, generationTime)
            .param(COMMENTS_FIELD_NAME, generationComments)
        
        if (((classRef = it.ref) == null) || ((classModel = it.target) == null)) {
            return
        }
        
        className = classRef.fullName()
        implClassName = implClass.fullName()
        classAnnos = classModelAnnosField.get(classRef)
        
        classRef.annotations().findAll{ (it.annotationClass.fullName() == JsonTypeName.class.name) ||
            (it.annotationClass.fullName() == XmlRootElement.class.name) }?.each{
            implAnnoModel = implClass.annotate(it.annotationClass)
            
            it.annotationMembers.each{
                annoModelAddValueMethod.invoke(implAnnoModel, it.key, it.value)
            }
            
            classAnnos.remove(it)
        }
        
        classRef.annotate(Generated).param(VALUE_MEMBER_NAME, generatorName).param(DATE_MEMBER_NAME, generationTime)
            .param(COMMENTS_FIELD_NAME, generationComments)
        
        if (implClass.abstract) {
            abstractImplClassNames.add(implClass.name())
        }
        
        fields = implClass.fields()
        classMethods = classRef.methods().stream().collect(Collectors.toMap({ it.name() }, { it }, { classMethod1, classMethod2 -> classMethod2 }))
        implClassMethods = implClass.methods().stream().collect(Collectors.toMap({ it.name() }, { it },
            { implClassMethod1, implClassMethod2 -> implClassMethod2 }))
        props = classModel.properties.stream().collect(Collectors.toMap({ it.getName(true) }, { it }, { prop1, prop2 -> prop2 }))
        
        props.values().each{
            if (!implClassMethods.containsKey((propGetterName = (GETTER_METHOD_NAME_PREFIX + (publicPropName = it.getName(true))))) &&
                !(propIsGetter = implClassMethods.containsKey((propGetterName = (IS_METHOD_NAME_PREFIX + publicPropName))))) {
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
                [ propGetter, implPropGetter ].each{ it.name((GETTER_METHOD_NAME_PREFIX + publicPropName)) }
            }
            
            if (implClassMethods.containsKey((propSetterName = (SETTER_METHOD_NAME_PREFIX + publicPropName)))) {
                if (classMethods.containsKey(propSetterName)) {
                    (propSetter = classMethods[propSetterName]).type(classRef)
                } else {
                    (propSetter = classRef.method(JMod.NONE, classRef, propSetterName)).param(propClass, privatePropName)
                }
                
                (implPropSetter = implClassMethods[propSetterName]).type(classRef)
                implPropSetter.body()._return(JExpr._this())
                
                if (StringUtils.startsWith(implPropSetter?.params()[0].type().fullName(), JAXBElement.class.name)) {
                    implPropSetter.annotate(SuppressWarnings.class).paramArray(VALUE_MEMBER_NAME).param(uncheckedStaticRef)
                } else {
                    propSetters.put(className, publicPropName, propSetter)
                    propSetters.put(implClassName, publicPropName, implPropSetter)
                }
            }
            
            if (it.collection) {
                (propAdder = classRef.method(JMod.NONE, classRef, (propAdderName = (ADD_METHOD_NAME + publicPropName)))).param(
                    (propItemClass = ((JClass) propClass).getTypeParameters()[0]), privatePropName);
                
                (implPropAdder = implClass.method(JMod.PUBLIC, classRef, propAdderName)).param(propItemClass, privatePropName)
                (implPropAdderBody = implPropAdder.body()).invoke(JExpr._this().invoke(implPropGetter), ADD_METHOD_NAME).arg(JExpr.direct(privatePropName))
                implPropAdderBody._return(JExpr._this())
                
                propAdders.put(className, publicPropName, propAdder)
                propAdders.put(implClassName, publicPropName, implPropAdder)
            }
            
            classRef.method(JMod.NONE, codeModel.BOOLEAN, (hasPropMethodName = (HAS_METHOD_NAME_PREFIX + publicPropName)));
            
            implClass.method(JMod.PUBLIC, codeModel.BOOLEAN, hasPropMethodName).body()._return(JExpr._this().ref(privatePropName)
                .ne((propClass.primitive ? ((propClass == codeModel.BOOLEAN) ? JExpr.lit(false) : JExpr.lit(0)) : JExpr._null())))
        }
    }
    
    it.classes?.each{
        if (((classModel = it.target) == null) || ((classRef = it.ref) == null) || ((implClass = it.implClass) == null)) {
            return
        }
        
        className = classRef.fullName()
        implClassName = implClass.fullName()
        superImplClass = implClass
        
        while ((superImplClassName = (superImplClass = superImplClass._extends()).fullName()) != Object.name) {
            if ((superClassName = (superClassOutline = outline.classes.find{ (it.implClass.fullName() == superImplClassName) })?.ref?.fullName()) == null) {
                continue
            }
            
            (superClassModel = superClassOutline.target).properties.each {
                publicPropName = it.getName(true)
                privatePropName = it.getName(false)

                if (propSetters.containsKey(superClassName, publicPropName)) {
                    superPropAccessor = propSetters.get(superClassName, publicPropName)
                    propSetterName = (SETTER_METHOD_NAME_PREFIX + publicPropName)

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

                if (it.collection && propAdders.get(superClassName, publicPropName)) {
                    superPropAccessor = propAdders.get(superClassName, publicPropName)
                    propAdderName = (ADD_METHOD_NAME + publicPropName)

                    if (propAdders.containsKey(className, publicPropName)) {
                        if ((propAdder = propAdders.get(className, publicPropName)).type().fullName() != className) {
                            propAdder.type(classRef)

                            propAdders.get(implClassName, publicPropName).type(classRef)
                        }
                    } else {
                        (propAdder = classRef.method(JMod.NONE, classRef, propAdderName))
                            .param(JMod.NONE, (superPropAccessorParamType = superPropAccessor.params()[0].type()), privatePropName)
                        propAdder.annotate(Override)

                        (implPropAdder = implClass.method(JMod.PUBLIC, classRef, propAdderName))
                            .param(JMod.NONE, superPropAccessorParamType, privatePropName)
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
    it.clazz.annotate(Generated).param(VALUE_MEMBER_NAME, generatorName).param(DATE_MEMBER_NAME, generationTime).param(COMMENTS_FIELD_NAME, generationComments)
}

try {
    codeModel.build(codeWriter)
} catch (e) {
    throw new MojoExecutionException("Unable to build code model for ${numBeans} class(es).", e)
}

log.info("Built code model for ${numBeans} class(es).")

def File srcFile
def srcFileBaseName
def srcFileContent

ant.fileset(dir: outDir, includes: "**/*${JAVA_SRC_FILE_EXT}").each{
    if ((srcFileBaseName = StringUtils.removeEnd((srcFile = it.file).name, JAVA_SRC_FILE_EXT)) == JAXB_CONTEXT_FACTORY_SIMPLE_CLASS_NAME) {
        srcFile.delete()
        
        return
    }
    
    srcFileContent = srcFile.text
    
    abstractImplClassNames.each{
        srcFileContent = srcFileContent.replaceAll(~/(\s+|\()(${it})(\s+|[\.\(\)])/, {
            it[1] + ABSTRACT_CLASS_NAME_PREFIX + StringUtils.removeEnd(it[2], IMPL_CLASS_NAME_SUFFIX) + it[3]
        })
    }
    
    srcFile.write(srcFileContent)
    
    if (abstractImplClassNames.contains(srcFileBaseName)) {
        srcFile.renameTo(new File(srcFile.parentFile,
            (ABSTRACT_CLASS_NAME_PREFIX + StringUtils.removeEnd(srcFileBaseName, IMPL_CLASS_NAME_SUFFIX) + JAVA_SRC_FILE_EXT)))
    }
}

ant.fileset(dir: outDir, includes: "**/jaxb.properties").each{
    it.file.text = MOXY_JAXB_CONTEXT_FACTORY_JAXB_PROPS_CONTENT
}
