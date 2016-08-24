package gov.hhs.onc.sdcct.validate.schema;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.Validatable;
import org.codehaus.stax2.validation.ValidationProblemHandler;
import org.codehaus.stax2.validation.XMLValidator;

public interface MsvValidatable extends Validatable {
    public void stopValidating() throws XMLStreamException;
    
    public boolean hasValidationProblemHandler();

    @Nullable
    public ValidationProblemHandler getValidationProblemHandler();

    @Nullable
    @Override
    public ValidationProblemHandler setValidationProblemHandler(@Nullable ValidationProblemHandler validationProblemHandler);
    
    public boolean hasValidator();
    
    @Nullable
    public XMLValidator getValidator();
}
