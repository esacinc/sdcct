import com.sun.codemodel.CodeWriter
import com.sun.codemodel.JCodeModel
import com.sun.tools.ws.processor.generator.CustomExceptionGenerator
import com.sun.tools.ws.processor.generator.GeneratorBase
import com.sun.tools.ws.processor.generator.SeiGenerator
import com.sun.tools.ws.processor.modeler.wsdl.WSDLModeler
import com.sun.tools.ws.wscompile.WSCodeWriter
import com.sun.tools.ws.wsdl.parser.MetadataFinder
import com.sun.tools.ws.wsdl.parser.WSDLInternalizationLogic
import com.sun.tools.xjc.ErrorReceiver
import com.sun.tools.xjc.Language
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.Plugin
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import com.sun.xml.ws.util.ServiceFinder
import gov.hhs.onc.sdcct.build.utils.SdcctBuildUtils
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.AccessorCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenCodeWriter
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenErrorReceiver
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenModelBuilder
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaCompiler
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenSchemaContext
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.CodegenWsOptions
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.GeneratedCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.HierarchyCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.ObjectFactoryCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.PackageCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.impl.RuntimeCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.naming.impl.CompositeNameConverter
import gov.hhs.onc.sdcct.build.xml.jaxb.term.impl.FhirTermCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.type.impl.FhirTypeCodegenPlugin
import gov.hhs.onc.sdcct.build.xml.jaxb.type.impl.RfdTypeCodegenPlugin
import gov.hhs.onc.sdcct.transform.impl.ResourceSourceResolverImpl
import gov.hhs.onc.sdcct.utils.SdcctClassUtils
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlResolverImpl
import org.apache.commons.collections4.BidiMap
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.maven.plugin.MojoExecutionException
import org.apache.tools.ant.types.FileSet

final List<String> DEFAULT_XJC_ARGS = [
    "-Xdefault-value",
    "-Xsetters",
    "-Xannotate",
    "-Xwildcard"
]

Map<String, String> bindingVars = ((Map<String, String>) this.binding.variables)
CodegenErrorReceiver errorReceiver = new CodegenErrorReceiver(log)
ErrorReceiver xjcErrorReceiver = errorReceiver.forXjc()
com.sun.tools.ws.wscompile.ErrorReceiver wsimportErrorReceiver = errorReceiver.forWsimport()

List<File> schemaFiles = SdcctBuildUtils.extractFiles(((FileSet) ant.fileset(dir: new File(project.properties.getProperty("project.build.schemaDirectory"))) {
    SdcctBuildUtils.tokenize(bindingVars["schemaIncludes"]).each {
        ant.include(name: it)
    }

    SdcctBuildUtils.tokenize(bindingVars["schemaExcludes"]).each {
        ant.exclude(name: it)
    }
}))

List<File> bindingFiles = SdcctBuildUtils.extractFiles(((FileSet) ant.fileset(dir: new File(project.properties.getProperty("project.build.jaxbDirectory"))) {
    SdcctBuildUtils.tokenize(bindingVars["bindingIncludes"]).each {
        ant.include(name: it)
    }

    SdcctBuildUtils.tokenize(bindingVars["bindingExcludes"]).each {
        ant.exclude(name: it)
    }
}))

File outDir = new File(bindingVars["outDir"])
outDir.mkdirs()
project.addCompileSourceRoot(outDir.path)

boolean debug = BooleanUtils.toBoolean(((String) bindingVars["debug"])), verbose = BooleanUtils.toBoolean(((String) bindingVars["verbose"])),
    wsimport = BooleanUtils.toBoolean(((String) bindingVars["wsimport"]))
BidiMap<String, String> pkgNames = new DualLinkedHashBidiMap<>(SdcctBuildUtils.tokenizeMap(bindingVars["pkgNames"])),
    implPkgNames = new DualLinkedHashBidiMap<>(((Map<String, String>) pkgNames.collectEntries{
    [ it.key, (it.value + ClassUtils.PACKAGE_SEPARATOR + SdcctClassUtils.IMPL_PKG_NAME) ] }))
String enc = project.properties["project.build.sourceEncoding"], basePkgName = pkgNames.values().iterator().next(),
    baseImplPkgName = implPkgNames.values().iterator().next()
CodegenSchemaContext schemaContext = new CodegenSchemaContext()
Options opts = new Options()
CodegenWsOptions wsOpts = null
CodegenSchemaCompiler schemaCompiler = null
List<String> args
JCodeModel codeModel = new JCodeModel()
Model model = null
Outline outline
CodeWriter codeWriter

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
        SdcctBuildUtils.tokenize(bindingVars["wsdlIncludes"]).each {
            ant.include(name: it)
        }

        SdcctBuildUtils.tokenize(bindingVars["wsdlExcludes"]).each {
            ant.exclude(name: it)
        }
    })).each{ wsOpts.addWSDL(it) }
    
    bindingFiles.each{ wsOpts.addBindings(it.path) }

    CollectionUtils.addAll((args = new ArrayList<String>(DEFAULT_XJC_ARGS)), SdcctBuildUtils.tokenize(bindingVars["args"]))
    args = args.collect{ ("-B" + it) }
} else {
    schemaFiles.each{ opts.addGrammar(it) }
    
    bindingFiles.each{ opts.addBindFile(it) }

    CollectionUtils.addAll((args = new ArrayList<String>(DEFAULT_XJC_ARGS)), SdcctBuildUtils.tokenize(bindingVars["args"]))
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

opts.entityResolver = new SdcctXmlResolverImpl(new ResourceSourceResolverImpl(), URI.create(((String) bindingVars["defaultBaseUri"])), true)

opts.setNameConverter(new CompositeNameConverter(schemaContext), null)

GeneratedCodegenPlugin genPlugin = new GeneratedCodegenPlugin(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext)
opts.allPlugins.add(genPlugin)
opts.activePlugins.add(genPlugin)

AccessorCodegenPlugin accessorPlugin = new AccessorCodegenPlugin(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext)
opts.allPlugins.add(accessorPlugin)
opts.activePlugins.add(accessorPlugin)

HierarchyCodegenPlugin hierarchyPlugin = new HierarchyCodegenPlugin(log, project, bindingVars, basePkgName, baseImplPkgName, codeModel, schemaContext)
opts.allPlugins.add(hierarchyPlugin)
opts.activePlugins.add(hierarchyPlugin)

PackageCodegenPlugin pkgPlugin = new PackageCodegenPlugin(log, project, bindingVars, pkgNames, implPkgNames, basePkgName, baseImplPkgName, codeModel,
    schemaContext)
opts.allPlugins.add(pkgPlugin)
opts.activePlugins.add(pkgPlugin)

ObjectFactoryCodegenPlugin objFactoryPlugin = new ObjectFactoryCodegenPlugin(log, project, bindingVars, pkgNames, implPkgNames, basePkgName, baseImplPkgName,
    codeModel, schemaContext)
opts.allPlugins.add(objFactoryPlugin)
opts.activePlugins.add(objFactoryPlugin)

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

log.info("Generated code outline for ${outline.classes.size()} class(es).")

try {
    codeModel.build(codeWriter)
} catch (e) {
    throw new MojoExecutionException("Unable to build code model for ${outline.classes.size()} class(es).", e)
}

log.info("Built code model for ${outline.classes.size()} class(es).")
