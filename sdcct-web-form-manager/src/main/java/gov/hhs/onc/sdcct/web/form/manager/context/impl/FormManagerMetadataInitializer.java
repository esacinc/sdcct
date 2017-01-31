package gov.hhs.onc.sdcct.web.form.manager.context.impl;

import gov.hhs.onc.sdcct.context.impl.AbstractMetadataInitializer;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order((Ordered.LOWEST_PRECEDENCE - 1))
public class FormManagerMetadataInitializer extends AbstractMetadataInitializer {
    public FormManagerMetadataInitializer(SdcctApplication app) {
        super(app, "sdcct-web-form-manager");
    }
}
