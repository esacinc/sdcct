package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.validate.SdcctValidator;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlOutputFactory;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.sf.saxon.om.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctValidator implements SdcctValidator {
    @Autowired
    protected SdcctXmlOutputFactory xmlOutFactory;

    @Autowired
    protected SdcctConfiguration config;

    protected ValidationType type;

    protected AbstractSdcctValidator(ValidationType type) {
        this.type = type;
    }

    @Override
    public List<ValidationIssue> validate(NodeInfo nodeInfo, JaxbComplexTypeMetadata<?> jaxbTypeMetadata, Class<?> beanClass,
        ResourceMetadata<?> resourceMetadata) throws ValidationException {
        List<ValidationIssue> issues = this.validateInternal(nodeInfo, jaxbTypeMetadata, beanClass, resourceMetadata, new ArrayList<>());

        Map<IssueLevel, Integer> issueLevelCounts =
            issues.stream().collect(Collectors.groupingBy(ValidationIssue::getLevel, Collectors.reducing(0, issue -> 1, Integer::sum)));
        int numErrorIssues = issueLevelCounts.getOrDefault(IssueLevel.ERROR, 0), numFatalIssues = issueLevelCounts.getOrDefault(IssueLevel.FATAL, 0);

        if ((numErrorIssues > 0) || (numFatalIssues > 0)) {
            throw new ValidationException(String.format(
                "XML node (nsPrefix=%s, nsUri=%s, localName=%s) validation (type=%s) failed (numInfoIssues=%d, numWarnIssues=%d, numErrorIssues=%d, numFatalIssues=%d).",
                nodeInfo.getPrefix(), nodeInfo.getURI(), nodeInfo.getLocalPart(), this.type.getId(), issueLevelCounts.getOrDefault(IssueLevel.INFORMATION, 0),
                issueLevelCounts.getOrDefault(IssueLevel.WARNING, 0), numErrorIssues, numFatalIssues), issues);
        }

        return issues;
    }

    protected abstract List<ValidationIssue> validateInternal(NodeInfo nodeInfo, JaxbComplexTypeMetadata<?> jaxbTypeMetadata, Class<?> beanClass,
        ResourceMetadata<?> resourceMetadata, List<ValidationIssue> issues) throws ValidationException;

    @Override
    public ValidationType getType() {
        return this.type;
    }
}
