package gov.hhs.onc.sdcct.rfd.form;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.rfd.form.impl.RfdFormImpl;
import gov.hhs.onc.sdcct.sdc.FormDesignType;

@JsonSubTypes({ @Type(RfdFormImpl.class) })
public interface RfdForm extends SdcctForm<FormDesignType> {
    @Override
    public RfdForm build() throws Exception;
}
