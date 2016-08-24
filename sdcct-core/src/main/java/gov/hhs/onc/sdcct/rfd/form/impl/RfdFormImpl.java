package gov.hhs.onc.sdcct.rfd.form.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.form.impl.AbstractSdcctForm;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;

public class RfdFormImpl extends AbstractSdcctForm<FormDesignType> implements RfdForm {
    public RfdFormImpl(String name, ResourceSource src) {
        super(SpecificationType.RFD, FormDesignType.class, FormDesignTypeImpl.class, name, src);
    }
}
