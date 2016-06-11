package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxInputFactory;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import org.codehaus.stax2.XMLStreamReader2;

public class SdcctXmlInputFactory extends WstxInputFactory {
    @Override
    public XMLStreamReader2 createXMLStreamReader(Source src) throws XMLStreamException {
        return ((XMLStreamReader2) super.createXMLStreamReader(src));
    }

    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
