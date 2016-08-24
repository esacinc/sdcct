package gov.hhs.onc.sdcct.test.impl;

import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.context.impl.SdcctApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;

public class SdcctApplicationContextLoader extends SpringBootContextLoader {
    @Override
    protected SdcctApplication getSpringApplication() {
        return SdcctApplicationConfiguration.buildApplication();
    }
}
