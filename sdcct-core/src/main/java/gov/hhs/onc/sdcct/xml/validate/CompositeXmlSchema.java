package gov.hhs.onc.sdcct.xml.validate;

import gov.hhs.onc.sdcct.xml.validate.impl.CompositeXmlValidator;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.ValidationContext;

public interface CompositeXmlSchema extends SdcctXmlSchema {
    @Override
    public CompositeXmlValidator createValidator(ValidationContext context) throws XMLStreamException;
}
