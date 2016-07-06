package gov.hhs.onc.sdcct.transform.content.path.impl;

import com.sun.msv.grammar.xmlschema.ComplexTypeExp;
import gov.hhs.onc.sdcct.transform.content.path.ElementPathSegment;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;

public class ElementPathSegmentImpl extends AbstractContentPathSegment<ComplexTypeExp, JaxbComplexTypeMetadata<?>> implements ElementPathSegment {
    private Integer index;

    public ElementPathSegmentImpl(@Nullable String nsPrefix, @Nullable String nsUri, String localName) {
        super(nsPrefix, nsUri, localName);
    }

    public ElementPathSegmentImpl(QName qname) {
        super(qname);
    }

    @Override
    public boolean hasIndex() {
        return (this.index != null);
    }

    @Nonnegative
    @Nullable
    @Override
    public Integer getIndex() {
        return this.index;
    }

    @Override
    public void setIndex(@Nonnegative @Nullable Integer index) {
        this.index = index;
    }
}
