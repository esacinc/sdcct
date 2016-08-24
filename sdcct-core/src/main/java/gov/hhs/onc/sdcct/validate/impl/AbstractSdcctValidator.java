package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.validate.SdcctValidator;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlOutputFactory;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import javax.annotation.Nullable;
import org.codehaus.stax2.validation.Validatable;
import org.codehaus.stax2.validation.XMLValidator;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctValidator implements SdcctValidator {
    @Autowired
    protected SdcctXmlOutputFactory xmlOutFactory;

    @Autowired
    protected SdcctConfiguration config;

    protected ValidationType type;
    protected boolean validateStream;
    protected boolean validateDoc;

    protected AbstractSdcctValidator(ValidationType type, boolean validateStream, boolean validateDoc) {
        this.type = type;
        this.validateStream = validateStream;
        this.validateDoc = validateDoc;
    }

    @Override
    public XdmDocument validateDocument(ValidationResult result, ResourceMetadata<?> resourceMetadata, XdmDocument doc) throws ValidationException {
        return doc;
    }

    @Nullable
    @Override
    public <T extends Validatable> XMLValidator validateStream(ValidationResult result, JaxbContextMetadata jaxbContextMetadata, T streamAccessor)
        throws ValidationException {
        return null;
    }

    @Override
    public ValidationType getType() {
        return this.type;
    }

    @Override
    public boolean getValidateDocument() {
        return this.validateDoc;
    }

    @Override
    public boolean getValidateStream() {
        return this.validateStream;
    }
}
