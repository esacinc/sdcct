package gov.hhs.onc.sdcct.xml.validate.impl;

import com.ctc.wstx.msv.GenericMsvValidator;
import gov.hhs.onc.sdcct.xml.validate.MsvXmlSchema;

public class MsvXmlValidator extends AbstractSdcctXmlValidator<MsvXmlSchema, GenericMsvValidator> {
    public MsvXmlValidator(GenericMsvValidator delegate) {
        this.delegate = delegate;
    }
}
