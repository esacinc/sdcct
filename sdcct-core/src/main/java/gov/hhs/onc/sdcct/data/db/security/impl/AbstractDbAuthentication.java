package gov.hhs.onc.sdcct.data.db.security.impl;

import gov.hhs.onc.sdcct.data.db.security.DbAuthentication;
import javax.annotation.Nullable;

public abstract class AbstractDbAuthentication implements DbAuthentication {
    protected String name;

    private final static long serialVersionUID = 0L;

    protected AbstractDbAuthentication(String name) {
        this.name = name;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
    }

    @Nullable
    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
