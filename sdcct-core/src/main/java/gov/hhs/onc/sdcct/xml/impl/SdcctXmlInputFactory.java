package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.sr.ValidatingStreamReader;
import com.ctc.wstx.stax.WstxInputFactory;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocatorImpl;
import gov.hhs.onc.sdcct.xml.logging.impl.LoggingXmlReporter;
import java.io.InputStream;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctXmlInputFactory extends WstxInputFactory {
    @Autowired
    private ContentPathBuilder contentPathBuilder;

    @Autowired
    private LoggingXmlReporter loggingReporter;

    @Override
    public SdcctXmlStreamReader createXMLStreamReader(Source src) throws XMLStreamException {
        return new SdcctXmlStreamReader(((ValidatingStreamReader) super.createXMLStreamReader(src)), new SdcctLocatorImpl(this.contentPathBuilder),
            this.loggingReporter);
    }

    @Override
    public SdcctXmlStreamReader createXMLStreamReader(InputStream inStream, String enc) throws XMLStreamException {
        return new SdcctXmlStreamReader(((ValidatingStreamReader) super.createXMLStreamReader(inStream, enc)), new SdcctLocatorImpl(this.contentPathBuilder),
            this.loggingReporter);
    }

    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
