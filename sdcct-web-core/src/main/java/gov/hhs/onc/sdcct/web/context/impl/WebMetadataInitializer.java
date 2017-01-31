package gov.hhs.onc.sdcct.web.context.impl;

import gov.hhs.onc.sdcct.context.impl.AbstractMetadataInitializer;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.LOWEST_PRECEDENCE)
public class WebMetadataInitializer extends AbstractMetadataInitializer {
    public WebMetadataInitializer(SdcctApplication app) {
        super(app, "sdcct-web");
    }
}
