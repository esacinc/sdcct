package gov.hhs.onc.sdcct.transform.location.impl;

import gov.hhs.onc.sdcct.transform.SdcctTransformException;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import gov.hhs.onc.sdcct.transform.content.path.ElementPathSegment;
import gov.hhs.onc.sdcct.transform.content.path.impl.AttributePathSegmentImpl;
import gov.hhs.onc.sdcct.transform.content.path.impl.ElementPathSegmentImpl;
import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SdcctLocatorImpl implements SdcctLocator {
    private static abstract class AbstractLocationNode {
        protected QName qname;

        protected AbstractLocationNode(QName qname) {
            this.qname = qname;
        }

        public QName getQname() {
            return this.qname;
        }
    }

    private static class LocationElement extends AbstractLocationNode {
        private MultiValueMap<QName, LocationElement> children = new LinkedMultiValueMap<>();

        public LocationElement(QName qname) {
            super(qname);
        }

        public MultiValueMap<QName, LocationElement> getChildren() {
            return this.children;
        }
    }

    private static class LocationAttribute extends AbstractLocationNode {
        public LocationAttribute(QName qname) {
            super(qname);
        }
    }

    private ContentPathBuilder contentPathBuilder;
    private LinkedList<LocationElement> elems = new LinkedList<>();
    private LocationAttribute attr;

    public SdcctLocatorImpl(ContentPathBuilder contentPathBuilder) {
        this.contentPathBuilder = contentPathBuilder;
    }

    @Override
    public SdcctLocation buildLocation(SdcctLocation loc) throws SdcctTransformException {
        if (this.elems.isEmpty()) {
            return loc;
        }

        QName locNodeQname;
        LinkedList<LocationElement> locElems = new LinkedList<>(this.elems);
        LinkedList<ContentPathSegment<?, ?>> locPathSegments = new LinkedList<>();

        if (this.attr != null) {
            locPathSegments.push(new AttributePathSegmentImpl((locNodeQname = this.attr.getQname())));

            loc.setAttributeQname(locNodeQname);
        }

        loc.setElementQname(locElems.peek().getQname());

        LocationElement locElem;
        ElementPathSegment locElemPathSegment;
        List<LocationElement> locSiblingElems;
        int numLocSiblingElems;

        while (!locElems.isEmpty()) {
            locPathSegments.push((locElemPathSegment = new ElementPathSegmentImpl((locNodeQname = (locElem = locElems.pop()).getQname()))));

            if (!locElems.isEmpty() && ((numLocSiblingElems = (locSiblingElems = locElems.peek().getChildren().get(locNodeQname)).size()) > 1)) {
                for (int a = 0; a < numLocSiblingElems; a++) {
                    if (locSiblingElems.get(a) == locElem) {
                        locElemPathSegment.setIndex(a);

                        break;
                    }
                }
            }
        }

        loc.setContentPath(this.contentPathBuilder.build(null, false, locPathSegments));

        return loc;
    }

    @Override
    public void setAttribute(@Nullable QName attrQname) {
        this.attr = ((attrQname != null) ? new LocationAttribute(attrQname) : null);
    }

    @Override
    public void popElement() {
        this.elems.pop();
    }

    @Override
    public void pushElement(QName elemQname) {
        LocationElement elem = new LocationElement(elemQname);

        if (!this.elems.isEmpty()) {
            this.elems.peek().getChildren().add(elemQname, elem);
        }

        this.elems.push(elem);
    }
}
