package gov.hhs.onc.sdcct.data.db.security;

import gov.hhs.onc.sdcct.beans.NamedBean;
import javax.annotation.Nullable;
import org.springframework.security.core.Authentication;

public interface DbAuthentication extends Authentication, NamedBean {
    @Nullable
    @Override
    public Object getDetails();

    @Override
    public String getPrincipal();
}
