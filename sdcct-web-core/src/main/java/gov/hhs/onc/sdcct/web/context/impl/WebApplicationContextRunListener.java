package gov.hhs.onc.sdcct.web.context.impl;

import gov.hhs.onc.sdcct.context.impl.AbstractSdcctApplicationRunListener;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order((Ordered.HIGHEST_PRECEDENCE + 2))
public class WebApplicationContextRunListener extends AbstractSdcctApplicationRunListener {
    public WebApplicationContextRunListener(SpringApplication app, String[] args) {
        super(app, args);
    }

    @Override
    public void started() {
        this.app.setApplicationContextClass(SdcctWebApplicationContext.class);
        this.app.setWebEnvironment(true);
    }
}
