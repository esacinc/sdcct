package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.beans.UriBean;
import java.net.URI;

public interface ValidationSource extends IdentifiedBean, NamedBean, UriBean {
    public boolean hasUri();

    @Override
    public URI getUri();
}
