package gov.hhs.onc.sdcct.xml.jaxb;

import gov.hhs.onc.sdcct.xml.jaxb.impl.JaxbResult;
import gov.hhs.onc.sdcct.xml.jaxb.impl.JaxbSource;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.InitializingBean;

public interface JaxbContextRepository extends InitializingBean {
    public <T> JaxbSource<T> buildSource(T src, @Nullable Map<String, Object> marshallerProps) throws JAXBException;

    public <T> JaxbResult<T> buildResult(Class<T> resultClass, @Nullable Map<String, Object> unmarshallerProps) throws JAXBException;

    public JaxbContextInfo findContextInfo(Class<?> clazz) throws JAXBException;

    public Map<JaxbContextId, JaxbContextInfo> getContextInfos();

    public void setContextInfos(JaxbContextInfo ... contextInfos);

    public Map<String, Object> getDefaultMarshallerProperties();

    public void setDefaultMarshallerProperties(Map<String, Object> defaultMarshallerProps);

    public Map<String, Object> getDefaultUnmarshallerProperties();

    public void setDefaultUnmarshallerProperties(Map<String, Object> defaultUnmarshallerProps);
}
