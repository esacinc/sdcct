package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory.SdcctXmlStreamReader;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;

public interface SdcctValidatorService {
    public XdmDocument validate(SdcctXmlStreamReader reader) throws ValidationException;
}
