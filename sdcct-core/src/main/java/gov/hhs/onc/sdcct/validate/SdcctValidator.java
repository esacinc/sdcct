package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import java.util.List;
import net.sf.saxon.om.NodeInfo;

public interface SdcctValidator {
    public List<ValidationIssue> validate(NodeInfo nodeInfo, JaxbComplexTypeMetadata<?> jaxbTypeMetadata, Class<?> beanClass,
        ResourceMetadata<?> resourceMetadata) throws ValidationException;

    public ValidationType getType();
}
