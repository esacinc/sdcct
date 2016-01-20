package gov.hhs.onc.sdcct.ws.impl;

import gov.hhs.onc.sdcct.logging.impl.TxTaskExecutor;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.provider.ServerProviderFactory;
import org.apache.cxf.jaxrs.spring.JAXRSServerFactoryBeanDefinitionParser.SpringJAXRSServerFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class SdcctJaxRsServerFactoryBean extends SpringJAXRSServerFactoryBean implements DisposableBean, SmartFactoryBean<Server> {
    private TxTaskExecutor txTaskExec;

    @Override
    public Server getObject() throws Exception {
        this.serviceFactory.setExecutor(this.txTaskExec);

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

    public TxTaskExecutor getTxTaskExecutor() {
        return this.txTaskExec;
    }

    public void setTxTaskExecutor(TxTaskExecutor txTaskExec) {
        this.txTaskExec = txTaskExec;
    }
}
