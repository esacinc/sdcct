package gov.hhs.onc.sdcct.transform.content.path;

import com.sun.msv.grammar.xmlschema.ComplexTypeExp;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface ElementPathSegment extends ContentPathSegment<ComplexTypeExp, JaxbComplexTypeMetadata<?>> {
    public boolean hasIndex();

    @Nonnegative
    @Nullable
    public Integer getIndex();

    public void setIndex(@Nonnegative @Nullable Integer index);
}
