package gov.hhs.onc.sdcct.crypto.impl;

import gov.hhs.onc.sdcct.beans.factory.impl.AbstractSdcctFactoryBean;
import java.security.Provider;

public abstract class AbstractCryptoFactoryBean<T> extends AbstractSdcctFactoryBean<T> {
    protected Provider prov;
    protected String type;

    protected AbstractCryptoFactoryBean(Class<T> beanClass) {
        super(beanClass);
    }

    public Provider getProvider() {
        return this.prov;
    }

    public void setProvider(Provider prov) {
        this.prov = prov;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
