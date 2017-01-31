package gov.hhs.onc.sdcct.validate.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataService;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctPullEventSource;
import gov.hhs.onc.sdcct.validate.SdcctValidator;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.validate.ValidationIssueSeverity;
import gov.hhs.onc.sdcct.validate.ValidationIssueTotals;
import gov.hhs.onc.sdcct.validate.ValidationIssues;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import gov.hhs.onc.sdcct.xml.SdcctXmlReporter;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlStreamReader;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.stream.Location;
import net.sf.saxon.evpull.StaxToEventBridge;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.tree.linked.ElementImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctValidatorServiceImpl implements SdcctValidatorService {
    @Autowired
    private List<SdcctValidator> validators;

    @Autowired
    private SdcctDocumentBuilder docBuilder;

    @Autowired
    private ContentPathBuilder contentPathBuilder;

    @Autowired
    private JaxbContextRepository jaxbContextRepo;

    @Autowired
    private SdcctSaxonConfiguration config;

    private Map<Class<?>, ResourceMetadata<?>> beanMetadatas;
    private JaxbContextMetadata jaxbContextMetadata;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctValidatorServiceImpl(ResourceMetadataService<?, ?> resourceMetadataService, JaxbContextMetadata jaxbContextMetadata) {
        this.beanMetadatas = ((Map<Class<?>, ResourceMetadata<?>>) resourceMetadataService.getBeanMetadatas());
        this.jaxbContextMetadata = jaxbContextMetadata;
    }

    @Override
    public XdmDocument validate(SdcctXmlStreamReader reader) throws ValidationException {
        SdcctXmlReporter<?> reporter = reader.getReporter();
        ValidationResult result = new ValidationResultImpl();
        ValidationIssueTotals issueTotals = result.setIssueTotals(new ValidationIssueTotalsImpl()).getIssueTotals();
        ValidationIssues issues = result.setIssues(new ValidationIssuesImpl()).getIssues();
        XdmDocument doc;

        for (SdcctValidator validator : this.validators) {
            if (!validator.getValidateStream()) {
                continue;
            }

            validator.validateStream(result, this.jaxbContextMetadata, reader);
        }

        StaxToEventBridge srcEventIterator = new StaxToEventBridge();
        srcEventIterator.setPipelineConfiguration(this.config.makePipelineConfiguration());
        srcEventIterator.setXMLStreamReader(reader);

        Location loc = reader.getLocation();

        try {
            doc = this.docBuilder.build(new SdcctPullEventSource(loc.getPublicId(), loc.getSystemId(), srcEventIterator));
        } catch (SaxonApiException e) {
            throw new ValidationException("Unable to build validated XML document.", e, result);
        } finally {
            reader.setReporter(reporter);
            reader.getValidator().setDelegate(null);
        }

        int numFatalIssues = buildIssueTotals(issueTotals, issues).getFatal(), numErrorIssues = issueTotals.getError();

        if ((numFatalIssues > 0) || (numErrorIssues > 0)) {
            throw new ValidationException(
                String.format("XML stream validation failed (numInfoIssues=%d, numWarnIssues=%d, numErrorIssues=%d, numFatalIssues=%d).",
                    issueTotals.getInformation(), issueTotals.getWarning(), numErrorIssues, numFatalIssues),
                result);
        }

        ElementImpl docElemInfo = doc.getDocumentElement();
        ContentPathSegment<?, ?> contentPathSegment;

        try {
            contentPathSegment = this.contentPathBuilder.build(this.jaxbContextRepo, true, docElemInfo).getSegments().getFirst();
        } catch (Exception e) {
            throw new ValidationException(String.format("Unable to build XML document element (nsPrefix=%s, nsUri=%s, localName=%s) content path.",
                docElemInfo.getPrefix(), docElemInfo.getURI(), docElemInfo.getLocalPart()), e, result);
        }

        ResourceMetadata<?> resourceMetadata = this.beanMetadatas.get(contentPathSegment.getBeanClass());

        if (resourceMetadata == null) {
            result.setStatus(true);

            return doc;
        }

        for (SdcctValidator validator : this.validators) {
            if (!validator.getValidateDocument()) {
                continue;
            }

            validator.validateDocument(result, resourceMetadata, doc);
        }

        if (((numFatalIssues = buildIssueTotals(issueTotals, issues).getFatal()) > 0) || ((numErrorIssues = issueTotals.getError()) > 0)) {
            throw new ValidationException(
                String.format("XML document validation failed (numInfoIssues=%d, numWarnIssues=%d, numErrorIssues=%d, numFatalIssues=%d).",
                    issueTotals.getInformation(), issueTotals.getWarning(), numErrorIssues, numFatalIssues),
                result);
        }

        result.setStatus(true);

        return doc;
    }

    private static ValidationIssueTotals buildIssueTotals(ValidationIssueTotals issueTotals, ValidationIssues issues) {
        Map<ValidationIssueSeverity, Integer> issueTotalsMap =
            issues.getIssues().stream().collect(Collectors.groupingBy(ValidationIssue::getSeverity, Collectors.reducing(0, issue -> 1, Integer::sum)));

        issueTotals.setAll(issueTotalsMap.size());
        issueTotals.setInformation(issueTotalsMap.getOrDefault(ValidationIssueSeverity.INFORMATION, 0));
        issueTotals.setWarning(issueTotalsMap.getOrDefault(ValidationIssueSeverity.WARNING, 0));
        issueTotals.setError(issueTotalsMap.getOrDefault(ValidationIssueSeverity.ERROR, 0));
        issueTotals.setFatal(issueTotalsMap.getOrDefault(ValidationIssueSeverity.FATAL, 0));

        return issueTotals;
    }
}
