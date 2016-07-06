package gov.hhs.onc.sdcct.transform.content.path.impl;

import com.sun.msv.grammar.xmlschema.SimpleTypeExp;
import gov.hhs.onc.sdcct.transform.content.path.AttributePathSegment;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSimpleTypeMetadata;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;

public class AttributePathSegmentImpl extends AbstractContentPathSegment<SimpleTypeExp, JaxbSimpleTypeMetadata<?>> implements AttributePathSegment {
    public AttributePathSegmentImpl(@Nullable String nsPrefix, @Nullable String nsUri, String localName) {
        super(nsPrefix, nsUri, localName);
    }

    public AttributePathSegmentImpl(QName qname) {
        super(qname);
    }
}
