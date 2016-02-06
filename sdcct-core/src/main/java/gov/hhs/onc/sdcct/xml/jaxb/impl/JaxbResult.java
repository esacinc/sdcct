package gov.hhs.onc.sdcct.xml.jaxb.impl;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.transform.sax.SAXResult;

public class JaxbResult<T> extends SAXResult {
    private Unmarshaller unmarshaller;
    private Class<T> resultClass;
    private UnmarshallerHandler unmarshallerHandler;

    public JaxbResult(Unmarshaller unmarshaller, Class<T> resultClass) {
        this(unmarshaller, resultClass, null);
    }

    public JaxbResult(Unmarshaller unmarshaller, Class<T> resultClass, @Nullable String sysId) {
        super();

        this.unmarshaller = unmarshaller;
        this.resultClass = resultClass;

        this.setHandler((this.unmarshallerHandler = this.unmarshaller.getUnmarshallerHandler()));
        this.setSystemId(sysId);
    }

    public T getResult() throws JAXBException {
        Object result = this.unmarshallerHandler.getResult();

        return resultClass.cast(((result instanceof JAXBElement<?>) ? ((JAXBElement<?>) result).getValue() : result));
    }
}
