package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxOutputFactory;
import gov.hhs.onc.sdcct.validate.schema.MsvValidatable;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import org.codehaus.stax2.XMLStreamWriter2;
import org.codehaus.stax2.util.StreamWriter2Delegate;
import org.codehaus.stax2.validation.ValidationProblemHandler;
import org.codehaus.stax2.validation.ValidatorPair;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidator;

public class SdcctXmlOutputFactory extends WstxOutputFactory {
    public static class SdcctXmlStreamWriter extends StreamWriter2Delegate implements MsvValidatable {
        private ValidationProblemHandler validationProblemHandler;
        private XMLValidator validator;

        public SdcctXmlStreamWriter(XMLStreamWriter2 delegate) {
            super(null);

            this.setParent(delegate);
        }

        @Override
        public void stopValidating() throws XMLStreamException {
            while (this.hasValidator()) {
                this.stopValidatingAgainst(this.validator);
            }
        }

        @Nullable
        @Override
        public XMLValidator stopValidatingAgainst(XMLValidationSchema validationSchema) throws XMLStreamException {
            XMLValidator removedValidator = super.stopValidatingAgainst(validator);
            XMLValidator[] validators = new XMLValidator[2];

            if (ValidatorPair.removeValidator(this.validator, validationSchema, validators)) {
                this.validator = validators[1];
            }

            return removedValidator;
        }

        @Nullable
        @Override
        public XMLValidator stopValidatingAgainst(XMLValidator validator) throws XMLStreamException {
            XMLValidator removedValidator = super.stopValidatingAgainst(validator);
            XMLValidator[] validators = new XMLValidator[2];

            if (ValidatorPair.removeValidator(this.validator, validator, validators)) {
                this.validator = validators[1];
            }

            return removedValidator;
        }

        @Nullable
        @Override
        public XMLValidator validateAgainst(XMLValidationSchema validationSchema) throws XMLStreamException {
            XMLValidator addedValidator = super.validateAgainst(validationSchema);

            this.validator = ((this.validator != null) ? new ValidatorPair(this.validator, addedValidator) : addedValidator);

            return addedValidator;
        }

        @Override
        public boolean hasValidationProblemHandler() {
            return (this.validationProblemHandler != null);
        }

        @Nullable
        @Override
        public ValidationProblemHandler getValidationProblemHandler() {
            return this.validationProblemHandler;
        }

        @Nullable
        @Override
        public ValidationProblemHandler setValidationProblemHandler(@Nullable ValidationProblemHandler validationProblemHandler) {
            return super.setValidationProblemHandler((this.validationProblemHandler = validationProblemHandler));
        }

        @Override
        public boolean hasValidator() {
            return (this.validator != null);
        }

        @Nullable
        @Override
        public XMLValidator getValidator() {
            return this.validator;
        }
    }

    @Override
    public SdcctXmlStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        return new SdcctXmlStreamWriter((XMLStreamWriter2) super.createXMLStreamWriter(result));
    }

    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
