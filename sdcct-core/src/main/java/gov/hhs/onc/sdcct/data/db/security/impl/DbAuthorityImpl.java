package gov.hhs.onc.sdcct.data.db.security.impl;

import gov.hhs.onc.sdcct.data.db.DbObjectType;
import gov.hhs.onc.sdcct.data.db.security.DbPrivilegeType;
import gov.hhs.onc.sdcct.data.db.security.DbAuthority;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class DbAuthorityImpl implements DbAuthority {
    private final static long serialVersionUID = 0L;

    private String objName;
    private DbObjectType objType;
    private Set<DbPrivilegeType> privileges;

    public DbAuthorityImpl(String objName, DbObjectType objType, DbPrivilegeType ... privileges) {
        this.objName = objName;
        this.objType = objType;
        this.privileges = Stream.of(privileges).collect(Collectors.toCollection(() -> EnumSet.noneOf(DbPrivilegeType.class)));
    }

    @Nullable
    @Override
    public String getAuthority() {
        return null;
    }

    @Override
    public String getObjectName() {
        return this.objName;
    }

    @Override
    public DbObjectType getObjectType() {
        return this.objType;
    }

    @Override
    public Set<DbPrivilegeType> getPrivileges() {
        return this.privileges;
    }
}
