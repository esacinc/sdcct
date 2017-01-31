package gov.hhs.onc.sdcct.web.form.filler.context.impl;

import gov.hhs.onc.sdcct.context.impl.AbstractMetadataInitializer;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order((Ordered.LOWEST_PRECEDENCE - 1))
public class FormFillerMetadataInitializer extends AbstractMetadataInitializer {
    public FormFillerMetadataInitializer(SdcctApplication app) {
        super(app, "sdcct-web-form-filler");
    }
}
