package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import com.sun.tools.xjc.ErrorReceiver
import com.sun.tools.xjc.api.ErrorListener
import java.util.regex.Pattern
import org.apache.maven.plugin.logging.Log
import org.xml.sax.SAXParseException

class CodegenErrorReceiver implements ErrorListener {
    private class XjcErrorReceiverAdapter extends ErrorReceiver {
        @Override
        void fatalError(SAXParseException cause) {
            CodegenErrorReceiver.this.fatalError(cause)
        }
        
        @Override
        void error(SAXParseException cause) {
            CodegenErrorReceiver.this.error(cause)
        }

        @Override
        void warning(SAXParseException cause) {
            CodegenErrorReceiver.this.warning(cause)
        }

        @Override
        void info(SAXParseException cause) {
            CodegenErrorReceiver.this.info(cause)
        }
    }
    
    private class WsimportErrorReceiverAdapter extends com.sun.tools.ws.wscompile.ErrorReceiver {
        private final static Pattern WSDL_MODELER_PORT_SOAP_BINDING_12_WARN_MSG_PATTERN = ~/^SOAP port "[^"]+": uses a non\-standard SOAP 1\.2 binding\.$/
        
        @Override
        void fatalError(SAXParseException cause) {
            CodegenErrorReceiver.this.fatalError(cause)
        }
        
        @Override
        void error(SAXParseException cause) {
            CodegenErrorReceiver.this.error(cause)
        }

        @Override
        void warning(SAXParseException cause) {
            String causeMsg = cause.message
    
            if ((causeMsg != null) && WSDL_MODELER_PORT_SOAP_BINDING_12_WARN_MSG_PATTERN.matcher(causeMsg)) {
                return;
            }
            
            CodegenErrorReceiver.this.warning(cause)
        }

        @Override
        void info(SAXParseException cause) {
            CodegenErrorReceiver.this.info(cause)
        }
        
        @Override
        void debug(SAXParseException cause) {
            CodegenErrorReceiver.this.info(cause)
        }
    }
    
    private final static String MSG_FORMAT = "Unable to generate code (publicId=%s, sysId=%s, lineNum=%d, columnNum=%d): %s"

    private Log log
    private XjcErrorReceiverAdapter xjcErrorReceiver = new XjcErrorReceiverAdapter()
    private WsimportErrorReceiverAdapter wsimportErrorReceiver = new WsimportErrorReceiverAdapter()

    CodegenErrorReceiver(Log log) {
        this.log = log
    }
    
    public com.sun.tools.ws.wscompile.ErrorReceiver forWsimport() {
        return this.wsimportErrorReceiver
    }
    
    public ErrorReceiver forXjc() {
        return this.xjcErrorReceiver
    }

    @Override
    void fatalError(SAXParseException cause) {
        this.log.error(buildMessage(cause), cause.cause)
    }

    @Override
    void error(SAXParseException cause) {
        this.log.error(buildMessage(cause), cause.cause)
    }

    @Override
    void warning(SAXParseException cause) {
        this.log.warn(buildMessage(cause), cause.cause)
    }

    @Override
    void info(SAXParseException cause) {
        this.log.info(buildMessage(cause), cause.cause)
    }

    private static String buildMessage(SAXParseException cause) {
        return String.format(MSG_FORMAT, cause.publicId, cause.systemId, cause.lineNumber, cause.columnNumber, cause.message)
    }
}
