package gov.hhs.onc.sdcct.xml.validate.impl;

import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.xml.qname.utils.SdcctQnameUtils;
import gov.hhs.onc.sdcct.xml.validate.CompositeXmlSchema;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import net.sf.saxon.s9api.QName;

public class CompositeXmlValidator extends AbstractSdcctXmlValidator<CompositeXmlSchema, MsvXmlValidator> {
    private SdcctLocator locator;

    public CompositeXmlValidator(SdcctLocator locator) {
        this.locator = locator;
    }

    @Override
    public int validateElementAndAttributes() throws XMLStreamException {
        this.locator.setAttribute(null);

        return super.validateElementAndAttributes();
    }

    @Nullable
    @Override
    public String validateAttribute(String localName, @Nullable String nsUri, @Nullable String nsPrefix, String value) throws XMLStreamException {
        this.locator.setAttribute(new QName(SdcctQnameUtils.build(nsPrefix, nsUri, localName)));

        return super.validateAttribute(localName, nsUri, nsPrefix, value);
    }

    @Nullable
    @Override
    public String validateAttribute(String localName, @Nullable String nsUri, @Nullable String nsPrefix, char[] valueChars, int valueStart, int valueEnd)
        throws XMLStreamException {
        this.locator.setAttribute(new QName(SdcctQnameUtils.build(nsPrefix, nsUri, localName)));

        return super.validateAttribute(localName, nsUri, nsPrefix, valueChars, valueStart, valueEnd);
    }

    @Override
    public int validateElementEnd(String localName, @Nullable String nsUri, @Nullable String nsPrefix) throws XMLStreamException {
        int allowedContentType = super.validateElementEnd(localName, nsUri, nsPrefix);

        this.locator.popElement();

        return allowedContentType;
    }

    @Override
    public void validateElementStart(String localName, @Nullable String nsUri, @Nullable String nsPrefix) throws XMLStreamException {
        this.locator.pushElement(new QName(SdcctQnameUtils.build(nsPrefix, nsUri, localName)));

        super.validateElementStart(localName, nsUri, nsPrefix);
    }
}
