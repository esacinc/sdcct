package gov.hhs.onc.sdcct.xml.jaxb.impl;

import gov.hhs.onc.sdcct.xml.jaxb.JaxbElementInfo;
import javax.xml.namespace.QName;

public class JaxbElementInfoImpl<T> implements JaxbElementInfo<T> {
    private Class<T> declType;
    private QName name;
    private Class<?> scope;

    public JaxbElementInfoImpl(Class<T> declType, QName name, Class<?> scope) {
        this.declType = declType;
        this.name = name;
        this.scope = scope;
    }

    @Override
    public Class<T> getDeclaredType() {
        return this.declType;
    }

    @Override
    public QName getName() {
        return this.name;
    }

    @Override
    public Class<?> getScope() {
        return this.scope;
    }
}
