package gov.hhs.onc.sdcct.web.tomcat.impl;

import org.apache.catalina.connector.RequestFacade;

public class TomcatRequestFacade extends RequestFacade {
    private TomcatRequest req;

    public TomcatRequestFacade(TomcatRequest req) {
        super(req);

        this.req = req;
    }

    public TomcatRequest getRequest() {
        return this.req;
    }
}
