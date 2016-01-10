package gov.hhs.onc.sdcct.web.test.impl;

import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.test.impl.SdcctApplicationContextLoader;
import gov.hhs.onc.sdcct.web.context.impl.SdcctWebApplicationContext;

public class SdcctWebApplicationContextLoader extends SdcctApplicationContextLoader {
    @Override
    protected SdcctApplication getSpringApplication() {
        SdcctApplication app = super.getSpringApplication();
        app.setApplicationContextClass(SdcctWebApplicationContext.class);
        app.setWebEnvironment(true);

        return app;
    }
}
