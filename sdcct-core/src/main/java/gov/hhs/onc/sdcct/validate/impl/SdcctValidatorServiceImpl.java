package gov.hhs.onc.sdcct.validate.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataService;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.validate.SdcctValidator;
import gov.hhs.onc.sdcct.validate.SdcctValidatorService;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationResult;
import gov.hhs.onc.sdcct.xml.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory.SdcctXmlStreamReader;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlStreamReaderEventIterator;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import java.util.List;
import java.util.Map;
import net.sf.saxon.evpull.PullEventSource;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.tree.linked.ElementImpl;
import org.apache.cxf.staxutils.FragmentStreamReader;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctValidatorServiceImpl implements SdcctValidatorService {
    @Autowired
    private List<SdcctValidator> validators;

    @Autowired
    private SdcctDocumentBuilder docBuilder;

    @Autowired
    private SdcctConfiguration config;

    private ContentPathBuilder contentPathBuilder;
    private Map<Class<?>, ResourceMetadata<?>> beanMetadatas;
    private JaxbContextMetadata jaxbContextMetadata;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctValidatorServiceImpl(ContentPathBuilder contentPathBuilder, ResourceMetadataService<?, ?> resourceMetadataService,
        JaxbContextMetadata jaxbContextMetadata) {
        this.contentPathBuilder = contentPathBuilder;
        this.beanMetadatas = ((Map<Class<?>, ResourceMetadata<?>>) resourceMetadataService.getBeanMetadatas());
        this.jaxbContextMetadata = jaxbContextMetadata;
    }

    @Override
    public XdmDocument validate(SdcctXmlStreamReader reader) throws ValidationException {
        ValidationResult result = new ValidationResultImpl();
        XdmDocument doc;

        for (SdcctValidator validator : this.validators) {
            if (!validator.getValidateStream()) {
                continue;
            }

            validator.validateStream(result, this.jaxbContextMetadata, reader);
        }

        FragmentStreamReader fragmentReader = new FragmentStreamReader(reader);
        fragmentReader.setAdvanceAtEnd(false);

        try {
            doc = this.docBuilder.build(new PullEventSource(new XmlStreamReaderEventIterator(fragmentReader, this.config.makePipelineConfiguration())));
        } catch (SaxonApiException e) {
            throw new ValidationException("Unable to build validated XML document.", e, result);
        } finally {
            reader.setValidationProblemHandler(null);
            reader.stopValidating();
        }

        Map<SdcctIssueSeverity, Integer> issueTotals = result.getIssueTotals();
        int numFatalIssues = issueTotals.getOrDefault(SdcctIssueSeverity.FATAL, 0), numErrorIssues = issueTotals.getOrDefault(SdcctIssueSeverity.ERROR, 0);

        if ((numFatalIssues > 0) || (numErrorIssues > 0)) {
            throw new ValidationException(
                String.format("XML stream validation failed (numInfoIssues=%d, numWarnIssues=%d, numErrorIssues=%d, numFatalIssues=%d).",
                    issueTotals.getOrDefault(SdcctIssueSeverity.INFORMATION, 0), issueTotals.getOrDefault(SdcctIssueSeverity.WARNING, 0), numErrorIssues,
                    numFatalIssues),
                result);
        }

        ElementImpl docElemInfo = doc.getDocumentElement();
        ContentPathSegment<?, ?> contentPathSegment;

        try {
            contentPathSegment = this.contentPathBuilder.build(true, docElemInfo).getSegments().getFirst();
        } catch (Exception e) {
            throw new ValidationException(String.format("Unable to build XML document element (nsPrefix=%s, nsUri=%s, localName=%s) content path.",
                docElemInfo.getPrefix(), docElemInfo.getURI(), docElemInfo.getLocalPart()), e, result);
        }

        ResourceMetadata<?> resourceMetadata = this.beanMetadatas.get(contentPathSegment.getBeanClass());

        for (SdcctValidator validator : this.validators) {
            if (!validator.getValidateDocument()) {
                continue;
            }

            validator.validateDocument(result, resourceMetadata, doc);
        }

        if (((numFatalIssues = (issueTotals = result.getIssueTotals()).getOrDefault(SdcctIssueSeverity.FATAL, 0)) > 0) ||
            ((numErrorIssues = issueTotals.getOrDefault(SdcctIssueSeverity.ERROR, 0)) > 0)) {
            throw new ValidationException(
                String.format("XML document validation failed (numInfoIssues=%d, numWarnIssues=%d, numErrorIssues=%d, numFatalIssues=%d).",
                    issueTotals.getOrDefault(SdcctIssueSeverity.INFORMATION, 0), issueTotals.getOrDefault(SdcctIssueSeverity.WARNING, 0), numErrorIssues,
                    numFatalIssues),
                result);
        }

        return doc;
    }
}
