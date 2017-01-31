package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.sr.ValidatingStreamReader;
import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.xml.SdcctXmlReporter;
import gov.hhs.onc.sdcct.xml.XmlStreamAccessor;
import gov.hhs.onc.sdcct.xml.validate.impl.CompositeXmlSchemaImpl;
import gov.hhs.onc.sdcct.xml.validate.impl.CompositeXmlValidator;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.util.StreamReader2Delegate;
import org.codehaus.stax2.validation.ValidationContext;

public class SdcctXmlStreamReader extends StreamReader2Delegate implements XmlStreamAccessor {
    private SdcctLocator locator;
    private CompositeXmlValidator validator;

    public SdcctXmlStreamReader(ValidatingStreamReader delegate, SdcctLocator locator, SdcctXmlReporter<?> reporter) throws XMLStreamException {
        super(delegate);

        this.validator = ((CompositeXmlValidator) this.validateAgainst(new CompositeXmlSchemaImpl((this.locator = locator))));

        this.setReporter(reporter);
    }

    @Override
    public SdcctLocator getLocator() {
        return this.locator;
    }

    @Override
    public SdcctXmlReporter<?> getReporter() {
        return ((SdcctXmlReporter<?>) this.getProperty(XMLInputFactory.REPORTER));
    }

    @Override
    public void setReporter(SdcctXmlReporter<?> reporter) {
        this.setProperty(XMLInputFactory.REPORTER, reporter);
    }

    @Override
    public ValidationContext getValidationContext() {
        return ((ValidatingStreamReader) ((this._delegate2 instanceof StreamReader2Delegate)
            ? ((StreamReader2Delegate) this._delegate2).getParent() : this._delegate2)).getInputElementStack();
    }

    @Override
    public CompositeXmlValidator getValidator() {
        return this.validator;
    }
}
