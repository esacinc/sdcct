package gov.hhs.onc.sdcct.validate.schema.impl;

import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import gov.hhs.onc.sdcct.transform.content.path.ElementPathSegment;
import gov.hhs.onc.sdcct.transform.content.path.impl.AttributePathSegmentImpl;
import gov.hhs.onc.sdcct.transform.content.path.impl.ElementPathSegmentImpl;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.ValidationContext;
import org.codehaus.stax2.validation.XMLValidationProblem;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MsvValidationContext implements ValidationContext {
    private static abstract class AbstractMsvNode {
        protected net.sf.saxon.s9api.QName qname;

        protected AbstractMsvNode(net.sf.saxon.s9api.QName qname) {
            this.qname = qname;
        }

        public net.sf.saxon.s9api.QName getQname() {
            return this.qname;
        }
    }

    private static class MsvAttribute extends AbstractMsvNode {
        public MsvAttribute(net.sf.saxon.s9api.QName qname) {
            super(qname);
        }
    }

    private static class MsvElement extends AbstractMsvNode {
        private MultiValueMap<net.sf.saxon.s9api.QName, MsvElement> children = new LinkedMultiValueMap<>();

        public MsvElement(net.sf.saxon.s9api.QName qname) {
            super(qname);
        }

        public MultiValueMap<net.sf.saxon.s9api.QName, MsvElement> getChildren() {
            return this.children;
        }
    }

    private ContentPathBuilder contentPathBuilder;
    private ValidationContext delegate;
    private LinkedList<MsvElement> elems = new LinkedList<>();
    private MsvAttribute attr;

    public MsvValidationContext(ContentPathBuilder contentPathBuilder, ValidationContext delegate) {
        this.contentPathBuilder = contentPathBuilder;
        this.delegate = delegate;
    }

    public void removeAttribute() {
        this.attr = null;
    }

    public void setAttribute(net.sf.saxon.s9api.QName attrQname) {
        this.attr = new MsvAttribute(attrQname);
    }

    public void popElement() {
        this.elems.pop();
    }

    public void pushElement(net.sf.saxon.s9api.QName elemQname) {
        MsvElement elem = new MsvElement(elemQname);

        if (!this.elems.isEmpty()) {
            this.elems.peek().getChildren().add(elemQname, elem);
        }

        this.elems.push(elem);
    }

    @Override
    public void reportProblem(XMLValidationProblem problem) throws XMLStreamException {
        SdcctLocation loc = new SdcctLocation(problem.getLocation());

        if (!this.elems.isEmpty()) {
            net.sf.saxon.s9api.QName problemNodeQname;
            LinkedList<MsvElement> problemElems = new LinkedList<>(this.elems);
            LinkedList<ContentPathSegment<?, ?>> problemPathSegments = new LinkedList<>();

            if (this.attr != null) {
                problemPathSegments.push(new AttributePathSegmentImpl((problemNodeQname = this.attr.getQname())));

                loc.setAttributeQname(problemNodeQname);
            }

            loc.setElementQname(problemElems.peek().getQname());

            MsvElement problemElem;
            ElementPathSegment problemElemPathSegment;
            List<MsvElement> problemSiblingElems;
            int numProblemSiblingElems;

            while (!problemElems.isEmpty()) {
                problemElemPathSegment = new ElementPathSegmentImpl((problemNodeQname = (problemElem = problemElems.pop()).getQname()));

                if (!problemElems.isEmpty() &&
                    ((numProblemSiblingElems = (problemSiblingElems = problemElems.peek().getChildren().get(problemNodeQname)).size()) > 1)) {
                    for (int a = 0; a < numProblemSiblingElems; a++) {
                        if (problemSiblingElems.get(a) == problemElem) {
                            problemElemPathSegment.setIndex(a);

                            break;
                        }
                    }
                }
            }

            try {
                loc.setContentPath(this.contentPathBuilder.build(false, problemPathSegments));
            } catch (Exception e) {
                throw new XMLStreamException(e);
            }
        }

        problem.setLocation(loc);

        this.delegate.reportProblem(problem);
    }

    @Override
    public String getNamespaceURI(String nsPrefix) {
        return this.delegate.getNamespaceURI(nsPrefix);
    }

    @Override
    public int getAttributeCount() {
        return this.delegate.getAttributeCount();
    }

    @Override
    public String getAttributeLocalName(int index) {
        return this.delegate.getAttributeLocalName(index);
    }

    @Override
    public String getAttributeNamespace(int index) {
        return this.delegate.getAttributeNamespace(index);
    }

    @Override
    public String getAttributePrefix(int index) {
        return this.delegate.getAttributePrefix(index);
    }

    @Override
    public String getAttributeValue(int index) {
        return this.delegate.getAttributeValue(index);
    }

    @Override
    public String getAttributeValue(String nsURI, String localName) {
        return this.delegate.getAttributeValue(nsURI, localName);
    }

    @Override
    public String getAttributeType(int index) {
        return this.delegate.getAttributeType(index);
    }

    @Override
    public int findAttributeIndex(String nsURI, String localName) {
        return this.delegate.findAttributeIndex(nsURI, localName);
    }

    @Override
    public int addDefaultAttribute(String localName, String uri, String prefix, String value) throws XMLStreamException {
        return this.delegate.addDefaultAttribute(localName, uri, prefix, value);
    }

    @Override
    public boolean isNotationDeclared(String name) {
        return this.delegate.isNotationDeclared(name);
    }

    @Override
    public boolean isUnparsedEntityDeclared(String name) {
        return this.delegate.isUnparsedEntityDeclared(name);
    }

    @Override
    public String getBaseUri() {
        return this.delegate.getBaseUri();
    }

    @Override
    public Location getValidationLocation() {
        return this.delegate.getValidationLocation();
    }

    @Override
    public QName getCurrentElementName() {
        return this.delegate.getCurrentElementName();
    }

    @Override
    public String getXmlVersion() {
        return this.delegate.getXmlVersion();
    }
}
