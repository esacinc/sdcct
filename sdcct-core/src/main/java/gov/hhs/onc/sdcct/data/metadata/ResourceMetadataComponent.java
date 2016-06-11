package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.beans.UriBean;
import gov.hhs.onc.sdcct.metadata.MetadataComponent;
import java.net.URI;
import javax.annotation.Nullable;

public interface ResourceMetadataComponent extends IdentifiedBean, MetadataComponent, SpecifiedBean, UriBean {
    public void setId(String id);

    public boolean hasUri();

    @Nullable
    @Override
    public URI getUri();

    public void setUri(@Nullable URI uri);
}
