package gov.hhs.onc.sdcct.web.gateway.context.impl;

import gov.hhs.onc.sdcct.context.impl.AbstractMetadataInitializer;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order((Ordered.LOWEST_PRECEDENCE - 2))
public class GatewayMetadataInitializer extends AbstractMetadataInitializer {
    public GatewayMetadataInitializer(SdcctApplication app) {
        super(app, "sdcct-web-gateway");
    }
}
