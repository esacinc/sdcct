package gov.hhs.onc.sdcct.xml.utils;

import gov.hhs.onc.sdcct.utils.SdcctIteratorUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.iterators.NodeListIterator;
import org.apache.cxf.helpers.DOMUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class SdcctDomUtils {
    private SdcctDomUtils() {
    }

    public static <T extends Map<String, Object>> T mapTreeContent(Supplier<T> contentMapSupplier, Stream<Element> elems) {
        return elems.collect(SdcctStreamUtils.toMap(Node::getLocalName, elem -> {
            List<Element> childElems = DomUtils.getChildElements(elem);

            return (!childElems.isEmpty() ? mapTreeContent(contentMapSupplier, childElems.stream()) : DOMUtils.getContent(elem));
        }, contentMapSupplier));
    }

    public static List<Element> findDescendantElements(@Nullable Node node, QName qname) {
        return streamDescendantElements(node).filter(namedNode(qname)).collect(Collectors.toList());
    }

    public static List<Element> findDescendantElements(@Nullable Node node, @Nullable String nsUri, String localName) {
        return streamDescendantElements(node).filter(namedNode(nsUri, localName)).collect(Collectors.toList());
    }

    @Nullable
    public static Element findDescendantElement(@Nullable Node node, QName qname) {
        return streamDescendantElements(node).filter(namedNode(qname)).findFirst().orElse(null);
    }

    @Nullable
    public static Element findDescendantElement(@Nullable Node node, @Nullable String nsUri, String localName) {
        return streamDescendantElements(node).filter(namedNode(nsUri, localName)).findFirst().orElse(null);
    }

    public static List<Element> findChildElements(@Nullable Node node, QName qname) {
        return streamChildElements(node).filter(namedNode(qname)).collect(Collectors.toList());
    }

    public static List<Element> findChildElements(@Nullable Node node, @Nullable String nsUri, String localName) {
        return streamChildElements(node).filter(namedNode(nsUri, localName)).collect(Collectors.toList());
    }

    @Nullable
    public static Element findChildElement(@Nullable Node node, QName qname) {
        return streamChildElements(node).filter(namedNode(qname)).findFirst().orElse(null);
    }

    @Nullable
    public static Element findChildElement(@Nullable Node node, @Nullable String nsUri, String localName) {
        return streamChildElements(node).filter(namedNode(nsUri, localName)).findFirst().orElse(null);
    }

    public static Predicate<? super Node> namedNode(QName qname) {
        return namedNode(qname.getNamespaceURI(), qname.getLocalPart());
    }

    public static Predicate<? super Node> namedNode(@Nullable String nsUri, String localName) {
        return node -> (Objects.equals(node.getNamespaceURI(), nsUri) && node.getLocalName().equals(localName));
    }

    public static Stream<Element> streamDescendantElements(@Nullable Node node) {
        return asElements(streamDescendantNodes(node));
    }

    public static Stream<Element> streamChildElements(@Nullable Node node) {
        return asElements(streamChildNodes(node));
    }

    public static Stream<Element> asElements(Stream<Node> stream) {
        return SdcctStreamUtils.asInstances(stream.filter(node -> (node.getNodeType() == Node.ELEMENT_NODE)), Element.class);
    }

    public static Stream<Node> streamDescendantNodes(@Nullable Node node) {
        return SdcctStreamUtils.traverse(IteratorUtils::nodeListIterator, false, node);
    }

    public static Stream<Node> streamChildNodes(@Nullable Node node) {
        if (node == null) {
            return Stream.empty();
        }

        NodeList childNodes = node.getChildNodes();

        return SdcctIteratorUtils.stream(new NodeListIterator(childNodes), childNodes.getLength());
    }
}
