package gov.hhs.onc.sdcct.xml.validate.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidator;

public abstract class AbstractSdcctXmlValidator<T extends XMLValidationSchema, U extends XMLValidator> extends XMLValidator {
    protected U delegate;

    @Override
    public void validationCompleted(boolean eod) throws XMLStreamException {
        if (this.hasDelegate()) {
            this.delegate.validationCompleted(eod);
        }
    }

    @Override
    public void validateText(String text, boolean lastTextSegment) throws XMLStreamException {
        if (this.hasDelegate()) {
            this.delegate.validateText(text, lastTextSegment);
        }
    }

    @Override
    public void validateText(char[] buffer, int textStart, int textEnd, boolean lastTextSegment) throws XMLStreamException {
        if (this.hasDelegate()) {
            this.delegate.validateText(buffer, textStart, textEnd, lastTextSegment);
        }
    }

    @Override
    public int validateElementAndAttributes() throws XMLStreamException {
        return (this.hasDelegate() ? this.delegate.validateElementAndAttributes() : CONTENT_ALLOW_ANY_TEXT);
    }

    @Nullable
    @Override
    public String validateAttribute(String localName, @Nullable String nsUri, @Nullable String nsPrefix, String value) throws XMLStreamException {
        return (this.hasDelegate() ? this.delegate.validateAttribute(localName, nsUri, nsPrefix, value) : null);
    }

    @Nullable
    @Override
    public String validateAttribute(String localName, @Nullable String nsUri, @Nullable String nsPrefix, char[] valueChars, int valueStart, int valueEnd)
        throws XMLStreamException {
        return (this.hasDelegate() ? this.delegate.validateAttribute(localName, nsUri, nsPrefix, valueChars, valueStart, valueEnd) : null);
    }

    @Override
    public int validateElementEnd(String localName, @Nullable String nsUri, @Nullable String nsPrefix) throws XMLStreamException {
        return (this.hasDelegate() ? this.delegate.validateElementEnd(localName, nsUri, nsPrefix) : CONTENT_ALLOW_ANY_TEXT);
    }

    @Override
    public void validateElementStart(String localName, @Nullable String nsUri, @Nullable String nsPrefix) throws XMLStreamException {
        if (this.hasDelegate()) {
            this.delegate.validateElementStart(localName, nsUri, nsPrefix);
        }
    }

    @Nullable
    @Override
    public String getAttributeType(int index) {
        return (this.hasDelegate() ? this.delegate.getAttributeType(index) : null);
    }

    public boolean hasDelegate() {
        return (this.delegate != null);
    }

    @Nullable
    public U getDelegate() {
        return this.delegate;
    }

    public void setDelegate(@Nullable U delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getIdAttrIndex() {
        return (this.hasDelegate() ? this.delegate.getIdAttrIndex() : -1);
    }

    @Override
    public int getNotationAttrIndex() {
        return (this.hasDelegate() ? this.delegate.getNotationAttrIndex() : -1);
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T getSchema() {
        return (this.hasDelegate() ? ((T) this.delegate.getSchema()) : null);
    }
}
