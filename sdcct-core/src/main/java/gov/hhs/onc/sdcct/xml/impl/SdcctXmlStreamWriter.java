package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.sw.SimpleNsStreamWriter;
import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.xml.SdcctXmlReporter;
import gov.hhs.onc.sdcct.xml.XmlStreamAccessor;
import gov.hhs.onc.sdcct.xml.validate.impl.CompositeXmlSchemaImpl;
import gov.hhs.onc.sdcct.xml.validate.impl.CompositeXmlValidator;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLStreamProperties;
import org.codehaus.stax2.util.StreamWriter2Delegate;
import org.codehaus.stax2.validation.ValidationContext;

public class SdcctXmlStreamWriter extends StreamWriter2Delegate implements XmlStreamAccessor {
    private SdcctLocator locator;
    private CompositeXmlValidator validator;

    public SdcctXmlStreamWriter(SimpleNsStreamWriter delegate, SdcctLocator locator, SdcctXmlReporter<?> reporter) throws XMLStreamException {
        super(null);

        this.setParent(delegate);

        this.validator = ((CompositeXmlValidator) this.validateAgainst(new CompositeXmlSchemaImpl((this.locator = locator))));

        this.setReporter(reporter);
    }

    @Override
    public SdcctLocator getLocator() {
        return this.locator;
    }

    @Override
    public SdcctXmlReporter<?> getReporter() {
        return ((SdcctXmlReporter<?>) this.getProperty(XMLStreamProperties.XSP_PROBLEM_REPORTER));
    }

    @Override
    public void setReporter(SdcctXmlReporter<?> reporter) {
        this.setProperty(XMLStreamProperties.XSP_PROBLEM_REPORTER, reporter);
    }

    @Override
    public ValidationContext getValidationContext() {
        return ((SimpleNsStreamWriter) this.mDelegate2);
    }

    @Override
    public CompositeXmlValidator getValidator() {
        return this.validator;
    }
}
