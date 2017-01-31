package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.xml.impl.SdcctXmlStreamReader;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;

public interface SdcctValidatorService {
    public XdmDocument validate(SdcctXmlStreamReader reader) throws ValidationException;
}
