package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.form.impl.AbstractSdcctForm;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;

public class RfdFormImpl extends AbstractSdcctForm<FormDesignType> implements RfdForm {
    public RfdFormImpl(String name, XdmDocument doc) {
        super(SpecificationType.RFD, FormDesignType.class, FormDesignTypeImpl.class, name, doc);
    }

    @Override
    public String getIdentifier() {
        return this.bean.getId();
    }
}
