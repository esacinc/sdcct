package gov.hhs.onc.sdcct.context.impl;

import gov.hhs.onc.sdcct.context.MetadataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetadataInitializerRunListener extends AbstractApplicationInitializerRunListener<MetadataInitializer> {
    private class DefaultMetadataInitializer extends AbstractMetadataInitializer {
        public DefaultMetadataInitializer() {
            super(MetadataInitializerRunListener.this.app, "sdcct");
        }
    }

    public MetadataInitializerRunListener(SpringApplication app, String[] args) {
        super(MetadataInitializer.class, app, args);
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment env) {
        this.buildInitializer(DefaultMetadataInitializer::new, this.app).initialize(env);
    }
}
