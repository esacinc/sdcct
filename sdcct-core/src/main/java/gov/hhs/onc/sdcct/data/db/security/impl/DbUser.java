package gov.hhs.onc.sdcct.data.db.security.impl;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;

public class DbUser extends AbstractDbAuthentication {
    private final static long serialVersionUID = 0L;

    private String pass;
    private Map<String, DbRole> roles;

    public DbUser(String name, String pass, DbRole ... roles) {
        super(name);

        this.pass = pass;
        this.roles = Stream.of(roles).collect(SdcctStreamUtils.toMap(NamedBean::getName, Function.identity(), () -> new LinkedHashMap<>(roles.length)));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }

    @Override
    public String getCredentials() {
        return this.pass;
    }

    public Map<String, DbRole> getRoles() {
        return this.roles;
    }
}
