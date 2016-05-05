package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.codemodel.JCodeModel
import com.sun.istack.SAXParseException2
import com.sun.tools.xjc.ErrorReceiver
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.api.ClassNameAllocator
import com.sun.tools.xjc.api.ErrorListener
import com.sun.tools.xjc.api.S2JJAXBModel
import com.sun.tools.xjc.api.SchemaCompiler
import com.sun.tools.xjc.api.SpecVersion
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import com.sun.tools.xjc.reader.internalizer.DOMForest
import com.sun.tools.xjc.reader.xmlschema.parser.LSInputSAXWrapper
import com.sun.tools.xjc.reader.xmlschema.parser.XMLSchemaInternalizationLogic
import com.sun.xml.bind.unmarshaller.DOMScanner
import com.sun.xml.bind.v2.util.XmlFactory
import gov.hhs.onc.sdcct.build.xml.jaxb.CodegenException
import java.lang.reflect.Constructor
import javax.annotation.Nullable
import javax.xml.XMLConstants
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader
import javax.xml.validation.SchemaFactory
import org.w3c.dom.Element
import org.w3c.dom.ls.LSInput
import org.w3c.dom.ls.LSResourceResolver
import org.xml.sax.ContentHandler
import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.LocatorImpl

class CodegenSchemaCompiler implements SchemaCompiler {
    private class SdcctLsResourceResolver implements LSResourceResolver {
        @Nullable
        @Override
        LSInput resolveResource(String type, String nsUri, String publicId, String sysId, String baseUri) {
            try {
                InputSource src = CodegenSchemaCompiler.this.opts.entityResolver.resolveEntity(nsUri, sysId)
                
                if (src != null) {
                    return new LSInputSAXWrapper(src)
                }
            } catch (IOException | SAXException ignored) {
            }
            
            return null
        }
    }
    
    private final static Constructor<? extends S2JJAXBModel> JAXB_MODEL_CONSTRUCTOR = buildJaxbModelConstructor("com.sun.tools.xjc.api.impl.s2j.JAXBModelImpl")
    
    private Options opts
    private ErrorReceiver errorReceiver
    private CodegenSchemaContext schemaContext
    private DOMForest domForest
    private JCodeModel codeModel
    private Model model
    private Outline outline
    private S2JJAXBModel jaxbModel

    CodegenSchemaCompiler(Options opts, ErrorReceiver errorReceiver, CodegenSchemaContext schemaContext) {
        this.opts = opts
        this.errorReceiver = errorReceiver
        this.schemaContext = schemaContext
        
        this.resetSchema()
    }

    @Nullable
    @Override
    S2JJAXBModel bind() {
        this.opts.bindFiles.each{ this.parseSchema(it) }
        
        SchemaFactory schemaFactory = XmlFactory.createSchemaFactory(XMLConstants.W3C_XML_SCHEMA_NS_URI, this.opts.disableXmlSecurity)
        schemaFactory.errorHandler = this.errorReceiver
        
        if (opts.entityResolver != null) {
            schemaFactory.resourceResolver = new SdcctLsResourceResolver()
        }
        
        this.domForest.weakSchemaCorrectnessCheck(schemaFactory)
        
        CodegenModelBuilder modelBuilder = new CodegenModelBuilder(this.opts, this.errorReceiver, (this.codeModel = new JCodeModel()), this.schemaContext)
        
        return ((((this.schemaContext = modelBuilder.buildSchemas(this.domForest, this.domForest.transform(this.opts.extensionMode))) != null) &&
            ((this.model = modelBuilder.buildModel()) != null) && ((this.outline = modelBuilder.buildOutline()) != null)) ?
            (this.jaxbModel = this.buildJaxbModel()) : null)
    }

    @Override
    void parseSchema(String sysId, Element elem) {
        try {
            LocatorImpl locator = new LocatorImpl()
            locator.systemId = sysId
            
            DOMScanner domScanner = new DOMScanner()
            domScanner.contentHandler = this.getParserHandler(sysId)
            domScanner.locator = locator
            
            domScanner.scan(elem)
        } catch (SAXException e) {
            this.errorReceiver.fatalError(new SAXParseException2(e.getMessage(), null, sysId, -1, -1, e))
        }
    }

    @Override
    void parseSchema(String sysId, XMLStreamReader reader) throws XMLStreamException {
        try {
            this.domForest.parse(sysId, reader, true)
        } catch (SAXException e) {
            this.errorReceiver.fatalError(new SAXParseException2(e.getMessage(), null, sysId, -1, -1, e))
        }
    }
    
    @Override
    void parseSchema(InputSource src) {
        try {
            this.domForest.parse(src, true)
        } catch (SAXException e) {
            this.errorReceiver.fatalError(new SAXParseException2(e.getMessage(), null, src.systemId, -1, -1, e))
        }
    }
    
    @Override
    void resetSchema() {
        (this.domForest = new DOMForest(new XMLSchemaInternalizationLogic(), this.opts)).entityResolver = this.opts.entityResolver
    }
    
    @Override
    void forcePackageName(String defaultPkgName) {
        this.opts.defaultPackage = defaultPkgName
    }
    
    private static Constructor<? extends S2JJAXBModel> buildJaxbModelConstructor(String className) {
        try {
            Constructor<? extends S2JJAXBModel> constructor = ((Constructor<? extends S2JJAXBModel>) Class.forName(className).declaredConstructors
                .find{ ((it.parameterCount == 1) && Outline.isAssignableFrom(it.parameterTypes[0])) })
            constructor.accessible = true
            
            return constructor
        } catch (Exception e) {
            throw new CodegenException(e)
        }
    }
    
    private S2JJAXBModel buildJaxbModel() {
        try {
            return JAXB_MODEL_CONSTRUCTOR.newInstance(this.outline)
        } catch (Exception e) {
            throw new CodegenException(e)
        }
    }
    
    @Override
    void setClassNameAllocator(ClassNameAllocator classNameAlloc) {
        this.opts.classNameAllocator = classNameAlloc
    }

    JCodeModel getCodeModel() {
        return this.codeModel
    }

    @Override
    void setDefaultPackageName(String defaultPkgName) {
        this.opts.defaultPackage2 = defaultPkgName
    }
    
    DOMForest getDomForest() {
        return this.domForest
    }
    
    @Override
    void setEntityResolver(EntityResolver entityResolver) {
        this.domForest.entityResolver = this.opts.entityResolver = entityResolver
    }
    
    @Override
    void setErrorListener(ErrorListener errorListener) {
    }

    S2JJAXBModel getJaxbModel() {
        return this.jaxbModel
    }

    Model getModel() {
        return this.model
    }

    @Override
    Options getOptions() {
        return this.opts
    }
    
    Outline getOutline() {
        return this.outline
    }
    
    @Override
    ContentHandler getParserHandler(String sysId) {
        return this.domForest.getParserHandler(sysId, true)
    }
    
    CodegenSchemaContext getSchemaContext() {
        return this.schemaContext
    }
    
    @Override
    void setTargetVersion(@Nullable SpecVersion targetVersion) {
        this.opts.target = Optional.ofNullable(targetVersion).orElse(SpecVersion.LATEST)
    }
}
