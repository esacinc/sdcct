package gov.hhs.onc.sdcct.xml.validate.impl;

import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.xml.validate.CompositeXmlSchema;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.ValidationContext;

public class CompositeXmlSchemaImpl extends AbstractSdcctXmlSchema implements CompositeXmlSchema {
    private SdcctLocator locator;

    public CompositeXmlSchemaImpl(SdcctLocator locator) {
        this.locator = locator;
    }

    @Override
    public CompositeXmlValidator createValidator(ValidationContext context) throws XMLStreamException {
        return new CompositeXmlValidator(this.locator);
    }

    @Nullable
    @Override
    public String getSchemaType() {
        return null;
    }
}
