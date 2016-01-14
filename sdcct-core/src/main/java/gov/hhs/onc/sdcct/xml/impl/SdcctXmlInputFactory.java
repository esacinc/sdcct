package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxInputFactory;
import java.util.Map;

public class SdcctXmlInputFactory extends WstxInputFactory {
    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
