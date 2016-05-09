package gov.hhs.onc.sdcct.data.db.security.impl;

import gov.hhs.onc.sdcct.data.db.security.DbAuthority;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class DbRole extends AbstractDbAuthentication {
    private final static long serialVersionUID = 0L;

    private Set<DbAuthority> authorities;

    public DbRole(String name, DbAuthority ... authorities) {
        super(name);

        this.authorities = Stream.of(authorities).collect(Collectors.toCollection(() -> new LinkedHashSet<>(authorities.length)));
    }

    @Override
    public Set<DbAuthority> getAuthorities() {
        return this.authorities;
    }

    @Nullable
    @Override
    public Object getCredentials() {
        return null;
    }
}
