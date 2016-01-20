package gov.hhs.onc.sdcct.ws.impl;

import gov.hhs.onc.sdcct.logging.impl.TxTaskExecutor;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;

public class SdcctJaxWsServerFactoryBean extends JaxWsServerFactoryBean implements DisposableBean, InitializingBean, SmartFactoryBean<Server> {
    private TxTaskExecutor txTaskExec;
    private Server server;

    @Override
    public Server getObject() throws Exception {
        return this.server;
    }

    @Override
    public void destroy() throws Exception {
        if (this.server != null) {
            this.server.destroy();

            this.server = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.getServiceFactory().setExecutor(this.txTaskExec);

        this.server = this.create();
    }

    @Override
    public boolean isEagerInit() {
        return false;
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
