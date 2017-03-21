package gov.hhs.onc.sdcct.rfd.form.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.form.impl.AbstractSdcctForm;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;

@JsonTypeName("rfdForm")
public class RfdFormImpl extends AbstractSdcctForm<FormDesignType> implements RfdForm {
    public RfdFormImpl(String name, ResourceSource src) {
        super(SpecificationType.RFD, FormDesignType.class, FormDesignTypeImpl.class, name, src);
    }

    @Override
    public RfdForm build() throws Exception {
        return ((RfdForm) super.build());
    }
}
