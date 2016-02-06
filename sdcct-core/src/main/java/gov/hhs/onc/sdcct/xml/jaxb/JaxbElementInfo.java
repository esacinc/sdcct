package gov.hhs.onc.sdcct.xml.jaxb;

import javax.xml.namespace.QName;

public interface JaxbElementInfo<T> {
    public Class<T> getDeclaredType();

    public QName getName();

    public Class<?> getScope();
}
