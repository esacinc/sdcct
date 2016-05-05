package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.codemodel.JCodeModel
import com.sun.tools.xjc.ErrorReceiver
import com.sun.tools.xjc.ModelLoader
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import com.sun.tools.xjc.reader.internalizer.DOMForest
import com.sun.tools.xjc.reader.internalizer.SCDBasedBindingSet
import com.sun.xml.xsom.XSSchemaSet
import javax.annotation.Nullable

class CodegenModelBuilder {
    private Options opts
    private ErrorReceiver errorReceiver
    private JCodeModel codeModel
    private CodegenSchemaContext schemaContext
    private ModelLoader delegate
    private XSSchemaSet schemas
    private Model model
    private Outline outline

    CodegenModelBuilder(Options opts, ErrorReceiver errorReceiver, JCodeModel codeModel, CodegenSchemaContext schemaContext) {
        this.delegate = new ModelLoader((this.opts = opts), (this.codeModel = codeModel), (this.errorReceiver = errorReceiver))
        this.schemaContext = schemaContext
    }
    
    @Nullable
    Outline buildOutline() {
        return ((this.model != null) ? (this.outline = this.model.generateCode(this.opts, this.errorReceiver)) : null)
    }
    
    @Nullable
    Model buildModel() {
        if ((this.schemas == null) || ((this.model = this.delegate.annotateXMLSchema(this.schemas)) == null)) {
            this.model.packageLevelAnnotations = this.opts.packageLevelAnnotations
        }
        
        return this.model
    }
    
    @Nullable
    CodegenSchemaContext buildSchemas(DOMForest domForest, SCDBasedBindingSet bindings) {
        return this.buildSchemaContext(this.delegate.createXSOM(domForest, bindings))
    }
    
    @Nullable
    CodegenSchemaContext buildSchemas() {
        return this.buildSchemaContext(this.delegate.loadXMLSchema())
    }
    
    private CodegenSchemaContext buildSchemaContext(@Nullable XSSchemaSet schemas) {
        if (schemas == null) {
            return null
        }
        
        this.schemaContext.initialize((this.schemas = schemas))
        
        this.opts.activePlugins.findAll{ (it instanceof AbstractCodegenPlugin) }.each{ ((AbstractCodegenPlugin) it).postProcessSchemas(this.opts, schemas) }
        
        return this.schemaContext
    }
}
