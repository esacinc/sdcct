package gov.hhs.onc.sdcct.ws.impl;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.provider.ServerProviderFactory;
import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class JaxRsServerFactoryBean extends SpringJAXRSServerFactoryBean implements DisposableBean, SmartFactoryBean<Server> {
    @Override
    public Server getObject() throws Exception {
        Server server = this.create();

        ((ServerProviderFactory) server.getEndpoint().get(ServerProviderFactory.class.getName())).setRequestPreprocessor(null);

        return server;
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
