package gov.hhs.onc.sdcct.ws.impl;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class SdcctJaxRsServerFactoryBean extends SpringJAXRSServerFactoryBean implements DisposableBean, SmartFactoryBean<Server> {
    @Override
    public Server getObject() throws Exception {
        return this.create();
    }

    @Override
    public boolean isEagerInit() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return Server.class;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
