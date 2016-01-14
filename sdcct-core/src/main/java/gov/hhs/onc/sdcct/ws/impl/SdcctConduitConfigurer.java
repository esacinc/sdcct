package gov.hhs.onc.sdcct.ws.impl;

import org.apache.cxf.Bus;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HTTPConduitConfigurer;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.InitializingBean;

public class SdcctConduitConfigurer implements HTTPConduitConfigurer, InitializingBean {
    private Bus bus;
    private HTTPClientPolicy clientPolicy;

    @Override
    public void configure(String name, String addr, HTTPConduit conduit) {
        conduit.setClient(this.clientPolicy);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.bus.setExtension(this, HTTPConduitConfigurer.class);
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public HTTPClientPolicy getClientPolicy() {
        return this.clientPolicy;
    }

    public void setClientPolicy(HTTPClientPolicy clientPolicy) {
        this.clientPolicy = clientPolicy;
    }
}
