package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.stax.WstxInputFactory;
import gov.hhs.onc.sdcct.validate.schema.MsvValidatable;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.util.StreamReader2Delegate;
import org.codehaus.stax2.validation.ValidationProblemHandler;
import org.codehaus.stax2.validation.ValidatorPair;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidator;

public class SdcctXmlInputFactory extends WstxInputFactory {
    public static class SdcctXmlStreamReader extends StreamReader2Delegate implements MsvValidatable {
        private ValidationProblemHandler validationProblemHandler;
        private XMLValidator validator;

        public SdcctXmlStreamReader(XMLStreamReader2 delegate) {
            super(delegate);
        }

        /**
         * Workaround for the fact that the {@link net.sf.saxon.evpull.StaxToEventBridge#translate Saxon StAX to event bridging} calls
         * {@link javax.xml.stream.XMLStreamReader#getText()} to get the data of a processing instruction.
         * 
         * TODO: Remove workaround once the changes from <a href="https://saxonica.plan.io/issues/2899">Saxon bug #2899</a> are available.
         */
        @Nullable
        @Override
        public String getText() {
            return ((this._delegate2.getEventType() == XMLStreamConstants.PROCESSING_INSTRUCTION) ? this._delegate2.getPIData() : super.getText());
        }

        @Override
        public void stopValidating() {
            while (this.hasValidator()) {
                try {
                    this.stopValidatingAgainst(this.validator);
                } catch (XMLStreamException e) {
                    throw new SaxonApiUncheckedException(e);
                }
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
    public SdcctXmlStreamReader createXMLStreamReader(Source src) throws XMLStreamException {
        return new SdcctXmlStreamReader(((XMLStreamReader2) super.createXMLStreamReader(src)));
    }

    public void setProperties(Map<String, Object> props) {
        props.forEach(this::setProperty);
    }
}
