package gov.hhs.onc.sdcct.xml.utils;

import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import org.apache.cxf.helpers.DOMUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class SdcctXmlUtils {
    private SdcctXmlUtils() {
    }

    public static <T extends Map<String, Object>> T mapTreeContent(Supplier<T> contentMapSupplier, Stream<Element> elems) {
        return elems.collect(SdcctStreamUtils.toMap(Node::getLocalName, elem -> {
            List<Element> childElems = DomUtils.getChildElements(elem);

            return (!childElems.isEmpty() ? mapTreeContent(contentMapSupplier, childElems.stream()) : DOMUtils.getContent(elem));
        }, contentMapSupplier));
    }

    public static List<Element> findElements(Element parentElem, QName elemQname) {
        return DOMUtils.findAllElementsByTagNameNS(parentElem, elemQname.getNamespaceURI(), elemQname.getLocalPart());
    }
}
