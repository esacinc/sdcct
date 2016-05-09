package gov.hhs.onc.sdcct.data.db.security;

import gov.hhs.onc.sdcct.data.db.DbObjectType;
import java.util.Set;
import javax.annotation.Nullable;
import org.springframework.security.core.GrantedAuthority;

public interface DbAuthority extends GrantedAuthority {
    @Nullable
    @Override
    public String getAuthority();

    public String getObjectName();

    public DbObjectType getObjectType();

    public Set<DbPrivilegeType> getPrivileges();
}
