package gov.hhs.onc.sdcct.web.context.impl;

import gov.hhs.onc.sdcct.context.impl.AbstractMetadataInitializer;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import java.io.File;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

@Order(Ordered.LOWEST_PRECEDENCE)
public class WebMetadataInitializer extends AbstractMetadataInitializer {
    public WebMetadataInitializer(SdcctApplication app) {
        super(app, "sdcct-web");
    }

    @Override
    protected File buildHomeDirectory(ConfigurableEnvironment env) {
        File homeDir = this.app.getHomeDirectory();

        return ((homeDir != null) ? this.buildHomeDirectory(homeDir) : super.buildHomeDirectory(env));
    }
}
