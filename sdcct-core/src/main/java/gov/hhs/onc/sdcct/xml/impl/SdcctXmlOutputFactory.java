package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxOutputFactory;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import org.codehaus.stax2.XMLStreamWriter2;

public class SdcctXmlOutputFactory extends WstxOutputFactory {
    @Override
    public XMLStreamWriter2 createXMLStreamWriter(Result result) throws XMLStreamException {
        return ((XMLStreamWriter2) super.createXMLStreamWriter(result));
    }

    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
