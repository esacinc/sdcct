import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import com.github.sebhoss.warnings.CompilerWarnings
import com.sun.codemodel.CodeWriter
import com.sun.codemodel.JAnnotationUse
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JDefinedClass
import com.sun.codemodel.JExpr
import com.sun.codemodel.JFieldVar
import com.sun.codemodel.JMethod
import com.sun.codemodel.JMod
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
import com.sun.tools.xjc.outline.Outline
import com.sun.xml.ws.util.ServiceFinder
import gov.hhs.onc.sdcct.tools.codegen.impl.CodegenErrorReceiver
import gov.hhs.onc.sdcct.tools.utils.SdcctToolUtils
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.xml.bind.JAXBElement
import javax.xml.bind.annotation.XmlNs
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSchema
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.MojoExecutionException

def final XJC_ARGS = [
    "-mark-generated",
    "-Xdefault-value",
    "-Xsetters",
    "-Xannotate",
    "-Xvalue-constructor"
]

def final JAVA_SRC_FILE_EXT = ".java"

def final IMPL_PKG_NAME_SUFFIX = ".impl"

def final ABSTRACT_CLASS_NAME_PREFIX = "Abstract"

def final IMPL_CLASS_NAME_SUFFIX = "Impl"

def final JAXB_CONTEXT_FACTORY_SIMPLE_CLASS_NAME = "JAXBContextFactory"

def final VALUE_FIELD_NAME = "value"

def final GETTER_METHOD_NAME_PREFIX = "get"
def final IS_METHOD_NAME_PREFIX = "is"
def final SETTER_METHOD_NAME_PREFIX = "set"

def errorReceiver = new CodegenErrorReceiver(log)
def xjcErrorReceiver = errorReceiver.forXjc()
def wsimportErrorReceiver = errorReceiver.forWsimport()
def bindingVars = this.binding.variables
def jaxbSrcDir = new File(project.properties.getProperty("project.build.jaxbSourceDirectory"))
def schemaSrcDir = new File(project.properties.getProperty("project.build.schemaSourceDirectory"))

def outDir = new File(outDir)
outDir.mkdirs()
project.addCompileSourceRoot(outDir.path)

def debug = BooleanUtils.toBoolean(((String)bindingVars["debug"]))
def enc = project.properties["project.build.sourceEncoding"]
def verbose = BooleanUtils.toBoolean(((String)bindingVars["verbose"]))
def wsimport = BooleanUtils.toBoolean(((String) bindingVars["wsimport"]))
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
        SdcctToolUtils.tokenize(((String)bindingVars["bindingIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String)bindingVars["bindingExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        wsimportOpts.addBindings(it.file.path)
    }

    (ant.fileset(dir: schemaSrcDir) {
        SdcctToolUtils.tokenize(((String)bindingVars["schemaIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String)bindingVars["schemaExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        wsimportOpts.addSchema(it.file)
    }

    (ant.fileset(dir: new File(project.properties.getProperty("project.build.wsdlSourceDirectory"))) {
        SdcctToolUtils.tokenize(((String)bindingVars["wsdlIncludes"])).each {
            ant.include(name: it)
        }

        SdcctToolUtils.tokenize(((String)bindingVars["wsdlExcludes"])).each {
            ant.exclude(name: it)
        }
    }).each {
        wsimportOpts.addWSDL(it.file)
    }

    CollectionUtils.addAll((args = XJC_ARGS.collect(new ArrayList<String>(), { ("-B" + it) })), SdcctToolUtils.tokenize(((String)bindingVars["args"])))
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

    CollectionUtils.addAll((args = new ArrayList<String>(XJC_ARGS)), SdcctToolUtils.tokenize(((String) bindingVars["args"])))
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
        plugins.each{
            wsimportOpts.activePlugins.add(it.forWsimport())
        }
        
        def metadataFinder = new MetadataFinder(new WSDLInternalizationLogic(), wsimportOpts, wsimportErrorReceiver)
        metadataFinder.parseWSDL()
        
        def wsdlModel = new WSDLModeler(wsimportOpts, wsimportErrorReceiver, metadataFinder).buildModel()
        def wsdlJaxbModel = wsdlModel.JAXBModel.s2JJAXBModel
        def wsdlJaxbModelClass = wsdlJaxbModel.class
        
        def wsdlJaxbModelModelField = wsdlJaxbModelClass.declaredFields.find{ (it.name == "model") }
        wsdlJaxbModelModelField.accessible = true
        model = ((Model) wsdlJaxbModelModelField.get(wsdlJaxbModel))
        
        def wsdlJaxbModelOutlineField = wsdlJaxbModelClass.declaredFields.find{ (it.name == "outline") }
        wsdlJaxbModelOutlineField.accessible = true
        outline = ((Outline) wsdlJaxbModelOutlineField.get(wsdlJaxbModel))
        
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
        model = ModelLoader.load(opts, (codeModel = new JCodeModel()), xjcErrorReceiver)
        outline = model.generateCode(opts, xjcErrorReceiver)
    }
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
def List<JAnnotationUse> classAnnos
def JAnnotationUse implAnnoModel
def Map<String, JFieldVar> fields
def Map<String, JMethod> classMethods
def Map<String, JMethod> implClassMethods
def Map<String, CPropertyInfo> props
def publicPropName
def propGetterName
def propIsGetter
def propGetter
def propClass
def implPropGetter
def implPropSetter
def propSetterName

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
    ].each{ it.objectFactory.annotate(SuppressWarnings.class).paramArray(VALUE_FIELD_NAME).param(rawTypesStaticRef).param(uncheckedStaticRef) }
    
    it.classes?.each{
        if (((classRef = it.ref) == null) || ((implClass = it.implClass) == null) || ((classModel = it.target) == null)) {
            return;
        }
        
        classAnnos = classModelAnnosField.get(classRef)
        
        classRef.annotations().findAll{ (it.annotationClass.fullName() == JsonTypeName.class.name) ||
            (it.annotationClass.fullName() == XmlRootElement.class.name) }?.each{
            implAnnoModel = implClass.annotate(it.annotationClass)
            
            it.annotationMembers.each{
                annoModelAddValueMethod.invoke(implAnnoModel, it.key, it.value)
            }
            
            classAnnos.remove(it)
        }
        
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
                    classMethods[propSetterName].type(classRef)
                } else {
                    classRef.method(JMod.NONE, classRef, propSetterName).param(propClass, "value")
                }
                
                (implPropSetter = implClassMethods[propSetterName]).type(classRef)
                implPropSetter.body()._return(JExpr._this())

                if (StringUtils.startsWith(implPropSetter?.params()[0].type().fullName(), JAXBElement.class.name)) {
                    implPropSetter.annotate(SuppressWarnings.class).paramArray(VALUE_FIELD_NAME).param(uncheckedStaticRef)
                }
            }
        }
    }
}

try {
    codeModel.build(codeWriter)
} catch (e) {
    throw new MojoExecutionException("Unable to build code model for ${numBeans} class(es).", e)
}

log.info("Built code model for ${numBeans} class(es).")

def srcFile
def srcFileBaseName
def srcFileContent

ant.fileset(dir: outDir, includes: "**/*${JAVA_SRC_FILE_EXT}").each{
    srcFileContent = (srcFile = it.file).text
    
    abstractImplClassNames.each {
        srcFileContent = srcFileContent.replaceAll(Pattern.compile("(\\s+)(${it})(\\s+|[\\.\\(])"), {
            it[1] + ABSTRACT_CLASS_NAME_PREFIX + StringUtils.removeEnd(it[2], IMPL_CLASS_NAME_SUFFIX) + it[3]
        })
    }
    
    if ((srcFileBaseName = StringUtils.removeEnd(srcFile.name, JAVA_SRC_FILE_EXT)) == JAXB_CONTEXT_FACTORY_SIMPLE_CLASS_NAME) {
        srcFileContent = srcFileContent.replaceFirst(Pattern.compile("(\\npublic\\s+class\\s+${JAXB_CONTEXT_FACTORY_SIMPLE_CLASS_NAME}\\s+)"),
        "\n@${SuppressWarnings.class.simpleName}({ ${compilerWarningsClassName}.${rawTypesFieldName}, ${compilerWarningsClassName}.${uncheckedFieldName} })\$1")
    }
    
    srcFile.write(srcFileContent)
    
    if (abstractImplClassNames.contains(srcFileBaseName)) {
        srcFile.renameTo(new File(srcFile.parentFile,
            (ABSTRACT_CLASS_NAME_PREFIX + StringUtils.removeEnd(srcFileBaseName, IMPL_CLASS_NAME_SUFFIX) + JAVA_SRC_FILE_EXT)))
    }
}
