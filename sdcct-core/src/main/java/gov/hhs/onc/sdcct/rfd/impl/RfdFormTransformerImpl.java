package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.form.transform.impl.AbstractFormTransformer;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.rfd.RfdFormTransformer;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;

public class RfdFormTransformerImpl extends AbstractFormTransformer<RfdForm> implements RfdFormTransformer {
    protected RfdFormTransformerImpl(ResourceSource src) {
        super(SpecificationType.RFD, RfdForm.class, RfdFormImpl.class, src);
    }
}
