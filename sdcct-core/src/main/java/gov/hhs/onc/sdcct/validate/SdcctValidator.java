package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import java.util.List;
import net.sf.saxon.tree.linked.DocumentImpl;
import net.sf.saxon.tree.linked.ElementImpl;

public interface SdcctValidator {
    public List<ValidationIssue> validate(DocumentImpl docInfo, ElementImpl docElemInfo, JaxbComplexTypeMetadata<?> jaxbTypeMetadata, Class<?> beanClass,
        ResourceMetadata<?> resourceMetadata) throws ValidationException;

    public ValidationType getType();
}
