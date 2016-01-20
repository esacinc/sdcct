package gov.hhs.onc.sdcct.web.tomcat.impl;

import org.apache.catalina.connector.ResponseFacade;

public class TomcatResponseFacade extends ResponseFacade {
    private TomcatResponse resp;

    public TomcatResponseFacade(TomcatResponse resp) {
        super(resp);

        this.resp = resp;
    }

    public TomcatResponse getResponse() {
        return this.resp;
    }
}
