package gov.hhs.onc.sdcct.validate.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataService;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import gov.hhs.onc.sdcct.validate.SdcctValidator;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.saxon.tree.linked.DocumentImpl;
import net.sf.saxon.tree.linked.ElementImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctValidatorServiceImpl implements SdcctValidatorService {
    @Autowired
    private List<SdcctValidator> validators;

    private ContentPathBuilder contentPathBuilder;
    private Map<Class<?>, ResourceMetadata<?>> beanMetadatas;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctValidatorServiceImpl(ContentPathBuilder contentPathBuilder, ResourceMetadataService<?, ?> resourceMetadataService) {
        this.contentPathBuilder = contentPathBuilder;
        this.beanMetadatas = ((Map<Class<?>, ResourceMetadata<?>>) resourceMetadataService.getBeanMetadatas());
    }

    @Override
    public void validate(XdmDocument doc) throws ValidationException {
        DocumentImpl docInfo = doc.getUnderlyingNode();
        ElementImpl docElemInfo = doc.getDocumentElement();

        List<ValidationIssue> issues = new ArrayList<>();
        ContentPathSegment<?, ?> contentPathSegment;

        try {
            contentPathSegment = this.contentPathBuilder.build(true, docElemInfo).getSegments().getFirst();
        } catch (Exception e) {
            throw new ValidationException(String.format("Unable to build XML document element (nsPrefix=%s, nsUri=%s, localName=%s) content path.",
                docElemInfo.getPrefix(), docElemInfo.getURI(), docElemInfo.getLocalPart()), e, issues);
        }

        JaxbComplexTypeMetadata<?> jaxbTypeMetadata = ((JaxbComplexTypeMetadata<?>) contentPathSegment.getJaxbTypeMetadata());
        Class<?> beanClass = contentPathSegment.getBeanClass();
        ResourceMetadata<?> resourceMetadata = this.beanMetadatas.get(beanClass);

        for (SdcctValidator validator : this.validators) {
            issues.addAll(validator.validate(docInfo, docElemInfo, jaxbTypeMetadata, beanClass, resourceMetadata));
        }
    }
}
