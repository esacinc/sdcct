package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxOutputFactory;
import java.util.Map;
import javax.xml.stream.XMLReporter;

public class SdcctXmlOutputFactory extends WstxOutputFactory {
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
