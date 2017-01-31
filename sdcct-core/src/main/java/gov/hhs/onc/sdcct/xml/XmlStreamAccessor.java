package gov.hhs.onc.sdcct.xml;

import gov.hhs.onc.sdcct.transform.location.SdcctLocator;
import gov.hhs.onc.sdcct.xml.validate.impl.CompositeXmlValidator;
import javax.xml.stream.Location;
import org.codehaus.stax2.validation.ValidationContext;

public interface XmlStreamAccessor {
    public Location getLocation();

    public SdcctLocator getLocator();

    public SdcctXmlReporter<?> getReporter();

    public void setReporter(SdcctXmlReporter<?> reporter);

    public ValidationContext getValidationContext();

    public CompositeXmlValidator getValidator();
}
