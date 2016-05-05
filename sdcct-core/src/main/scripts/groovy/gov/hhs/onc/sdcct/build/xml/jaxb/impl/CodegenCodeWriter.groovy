package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.codemodel.CodeWriter
import com.sun.codemodel.JPackage
import com.sun.tools.xjc.runtime.JAXBContextFactory
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.output.NullOutputStream

class CodegenCodeWriter extends CodeWriter {
    private final static String JAXB_CONTEXT_FACTORY_JAVA_SRC_FILE_NAME = (JAXBContextFactory.simpleName + FilenameUtils.EXTENSION_SEPARATOR +
        SdcctFileNameExtensions.JAVA)
    
    private CodeWriter delegate

    CodegenCodeWriter(CodeWriter delegate) {
        this.delegate = delegate
    }

    @Override
    OutputStream openBinary(JPackage pkgModel, String fileName) throws IOException {
        return ((fileName != JAXB_CONTEXT_FACTORY_JAVA_SRC_FILE_NAME) ? this.delegate.openBinary(pkgModel, fileName) : NullOutputStream.NULL_OUTPUT_STREAM)
    }

    @Override
    void close() throws IOException {
        this.delegate.close()
    }
}
