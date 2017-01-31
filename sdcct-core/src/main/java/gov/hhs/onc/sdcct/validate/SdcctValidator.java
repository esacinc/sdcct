package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.xml.XmlStreamAccessor;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import javax.annotation.Nullable;

public interface SdcctValidator {
    public XdmDocument validateDocument(ValidationResult result, ResourceMetadata<?> resourceMetadata, XdmDocument doc) throws ValidationException;

    @Nullable
    public <T extends XmlStreamAccessor> T validateStream(ValidationResult result, JaxbContextMetadata jaxbContextMetadata, T streamAccessor)
        throws ValidationException;

    public ValidationType getType();

    public boolean getValidateDocument();

    public boolean getValidateStream();
}
