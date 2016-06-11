package gov.hhs.onc.sdcct.xml.xslt;

import gov.hhs.onc.sdcct.xml.DynamicXmlTransformOptions;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import net.sf.saxon.s9api.Destination;

public interface DynamicXsltOptions extends DynamicXmlTransformOptions<DynamicXsltOptions> {
    public boolean hasDestination();

    @Nullable
    public Destination getDestination();

    public DynamicXsltOptions setDestination(@Nullable Destination dest);

    public boolean hasSource();

    @Nullable
    public Source getSource();

    public DynamicXsltOptions setSource(@Nullable Source src);
}
