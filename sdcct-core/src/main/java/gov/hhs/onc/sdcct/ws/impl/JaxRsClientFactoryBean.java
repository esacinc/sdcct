package gov.hhs.onc.sdcct.ws.impl;

import org.apache.cxf.jaxrs.client.spring.JAXRSClientFactoryBeanDefinitionParser.JAXRSSpringClientFactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class JaxRsClientFactoryBean extends JAXRSSpringClientFactoryBean implements SmartFactoryBean<JaxRsClient> {
    public JaxRsClientFactoryBean() {
        super();
    }

    public JaxRsClientFactoryBean(String addr) {
        super();

        this.setAddress(addr);
    }

    @Override
    public JaxRsClient getObject() throws Exception {
        return new JaxRsClient(this.create());
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }

    @Override
    public Class<?> getObjectType() {
        return JaxRsClient.class;
    }

    @Override
    public boolean isPrototype() {
        return true;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
