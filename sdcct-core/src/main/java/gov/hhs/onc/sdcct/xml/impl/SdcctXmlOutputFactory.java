package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxOutputFactory;
import com.ctc.wstx.sw.SimpleNsStreamWriter;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocatorImpl;
import gov.hhs.onc.sdcct.xml.logging.impl.LoggingXmlReporter;
import java.io.OutputStream;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctXmlOutputFactory extends WstxOutputFactory {
    @Autowired
    private ContentPathBuilder contentPathBuilder;

    @Autowired
    private LoggingXmlReporter loggingReporter;

    @Override
    public SdcctXmlStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        return new SdcctXmlStreamWriter((SimpleNsStreamWriter) super.createXMLStreamWriter(result), new SdcctLocatorImpl(this.contentPathBuilder),
            this.loggingReporter);
    }

    @Override
    public SdcctXmlStreamWriter createXMLStreamWriter(OutputStream outStream, String enc) throws XMLStreamException {
        return new SdcctXmlStreamWriter((SimpleNsStreamWriter) super.createXMLStreamWriter(outStream, enc), new SdcctLocatorImpl(this.contentPathBuilder),
            this.loggingReporter);
    }

    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
