package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.xml.impl.XdmDocument;

public interface SdcctValidatorService {
    public void validate(XdmDocument doc) throws ValidationException;
}
