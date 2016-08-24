package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import javax.annotation.Nullable;
import org.codehaus.stax2.validation.Validatable;
import org.codehaus.stax2.validation.XMLValidator;

public interface SdcctValidator {
    public XdmDocument validateDocument(ValidationResult result, ResourceMetadata<?> resourceMetadata, XdmDocument doc) throws ValidationException;

    @Nullable
    public <T extends Validatable> XMLValidator validateStream(ValidationResult result, JaxbContextMetadata jaxbContextMetadata, T streamAccessor)
        throws ValidationException;

    public ValidationType getType();

    public boolean getValidateDocument();

    public boolean getValidateStream();
}
