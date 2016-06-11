package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxOutputFactory;
import java.util.Map;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import org.codehaus.stax2.XMLStreamWriter2;

public class SdcctXmlOutputFactory extends WstxOutputFactory {
    @Override
    public XMLStreamWriter2 createXMLStreamWriter(Result result) throws XMLStreamException {
        return ((XMLStreamWriter2) super.createXMLStreamWriter(result));
    }

    public XMLReporter getProblemReporter() {
        return this.mConfig.getProblemReporter();
    }

    public void setProblemReporter(XMLReporter probReporter) {
        this.mConfig.setProblemReporter(probReporter);
    }

    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
