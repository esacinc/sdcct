package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.tools.ws.wscompile.WsimportOptions
import com.sun.tools.xjc.ErrorReceiver
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.api.SchemaCompiler
import gov.hhs.onc.sdcct.build.xml.jaxb.CodegenException
import java.lang.reflect.Field

class CodegenWsOptions extends WsimportOptions {
    CodegenWsOptions(Options opts, ErrorReceiver errorReceiver, CodegenSchemaContext schemaContext) {
        try {
            Field schemaCompilerField = WsimportOptions.declaredFields.find{ SchemaCompiler.isAssignableFrom(it.type) }
            schemaCompilerField.accessible = true
            
            schemaCompilerField.set(this, new CodegenSchemaCompiler(opts, errorReceiver, schemaContext))
        } catch (Exception e) {
            throw new CodegenException(e)
        }
    }

    @Override
    CodegenSchemaCompiler getSchemaCompiler() {
        return ((CodegenSchemaCompiler) super.getSchemaCompiler())
    }
}
