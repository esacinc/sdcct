package gov.hhs.onc.sdcct.validate.schema.impl;

import com.ctc.wstx.msv.GenericMsvValidator;
import com.sun.msv.verifier.regexp.StringToken;
import com.sun.msv.verifier.regexp.xmlschema.XSREDocDecl;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlQnameUtils;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.validation.XMLValidator;
import org.relaxng.datatype.Datatype;

public class MsvValidator extends XMLValidator {
    private MsvValidationContext context;
    private GenericMsvValidator delegate;

    public MsvValidator(MsvValidationSchema schema, MsvValidationContext context) {
        this.context = context;
        this.delegate = new GenericMsvValidator(schema, context, new XSREDocDecl(schema.getGrammar()));
    }

    @Override
    public void validationCompleted(boolean eod) throws XMLStreamException {
        this.delegate.validationCompleted(eod);
    }

    @Override
    public void validateText(String text, boolean lastTextSegment) throws XMLStreamException {
        this.delegate.validateText(text, lastTextSegment);
    }

    @Override
    public void validateText(char[] buffer, int textStart, int textEnd, boolean lastTextSegment) throws XMLStreamException {
        this.delegate.validateText(buffer, textStart, textEnd, lastTextSegment);
    }

    @Override
    public int validateElementAndAttributes() throws XMLStreamException {
        this.context.removeAttribute();

        return this.delegate.validateElementAndAttributes();
    }

    @Nullable
    @Override
    public String validateAttribute(String localName, @Nullable String nsUri, @Nullable String nsPrefix, String value) throws XMLStreamException {
        this.context.setAttribute(SdcctXmlQnameUtils.build(nsPrefix, nsUri, localName));

        return this.delegate.validateAttribute(localName, nsUri, nsPrefix, value);
    }

    @Nullable
    @Override
    public String validateAttribute(String localName, @Nullable String nsUri, @Nullable String nsPrefix, char[] valueChars, int valueStart, int valueEnd)
        throws XMLStreamException {
        this.context.setAttribute(SdcctXmlQnameUtils.build(nsPrefix, nsUri, localName));

        return this.delegate.validateAttribute(localName, nsUri, nsPrefix, valueChars, valueStart, valueEnd);
    }

    @Override
    public int validateElementEnd(String localName, @Nullable String nsUri, @Nullable String nsPrefix) throws XMLStreamException {
        this.context.popElement();

        return this.delegate.validateElementEnd(localName, nsUri, nsPrefix);
    }

    @Override
    public void validateElementStart(String localName, @Nullable String nsUri, @Nullable String nsPrefix) throws XMLStreamException {
        this.context.pushElement(SdcctXmlQnameUtils.build(nsPrefix, nsUri, localName));

        this.delegate.validateElementStart(localName, nsUri, nsPrefix);
    }

    public void onID(Datatype datatype, StringToken idToken) throws IllegalArgumentException {
        this.delegate.onID(datatype, idToken);
    }

    @Nullable
    @Override
    public String getAttributeType(int index) {
        return this.delegate.getAttributeType(index);
    }

    public boolean isNotation(String notationName) {
        return this.delegate.isNotation(notationName);
    }

    public boolean isUnparsedEntity(String entityName) {
        return this.delegate.isUnparsedEntity(entityName);
    }

    public String resolveNamespacePrefix(String nsPrefix) {
        return this.delegate.resolveNamespacePrefix(nsPrefix);
    }

    public String getBaseUri() {
        return this.delegate.getBaseUri();
    }

    public MsvValidationContext getContext() {
        return this.context;
    }

    @Override
    public int getIdAttrIndex() {
        return this.delegate.getIdAttrIndex();
    }

    @Override
    public int getNotationAttrIndex() {
        return this.delegate.getNotationAttrIndex();
    }

    @Override
    public MsvValidationSchema getSchema() {
        return ((MsvValidationSchema) this.delegate.getSchema());
    }

    @Override
    public String getSchemaType() {
        return this.delegate.getSchemaType();
    }
}
