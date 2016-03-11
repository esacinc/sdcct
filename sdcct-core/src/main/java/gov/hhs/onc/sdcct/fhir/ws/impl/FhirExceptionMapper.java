package gov.hhs.onc.sdcct.fhir.ws.impl;

import gov.hhs.onc.sdcct.fhir.CodeableConcept;
import gov.hhs.onc.sdcct.fhir.Coding;
import gov.hhs.onc.sdcct.fhir.FhirException;
import gov.hhs.onc.sdcct.fhir.FhirVocabRepository;
import gov.hhs.onc.sdcct.fhir.IssueSeverityList;
import gov.hhs.onc.sdcct.fhir.IssueTypeList;
import gov.hhs.onc.sdcct.fhir.NarrativeStatusList;
import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeIssue;
import gov.hhs.onc.sdcct.fhir.impl.CodeableConceptImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodingImpl;
import gov.hhs.onc.sdcct.fhir.impl.IssueSeverityImpl;
import gov.hhs.onc.sdcct.fhir.impl.IssueTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeImpl;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeStatusImpl;
import gov.hhs.onc.sdcct.fhir.impl.OperationOutcomeImpl;
import gov.hhs.onc.sdcct.fhir.impl.OperationOutcomeIssueImpl;
import gov.hhs.onc.sdcct.fhir.impl.StringValueImpl;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.DivImpl;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.PreImpl;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.nio.charset.StandardCharsets;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("provExceptionMapperFhir")
@Priority(0)
public class FhirExceptionMapper implements ExceptionMapper<Throwable> {
    private final static String OP_OUTCOME_VALUE_SET_ID = "operation-outcome";

    private final static Logger LOGGER = LoggerFactory.getLogger(FhirExceptionMapper.class);

    @Autowired
    private FhirVocabRepository vocabRepo;

    @Autowired
    private XmlCodec xmlCodec;

    @Override
    public Response toResponse(Throwable exception) {
        IssueTypeList issueType = IssueTypeList.EXCEPTION;
        CodeableConcept issueDetailConcept = null;
        Status respStatus = Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof FhirException) {
            FhirException fhirException = ((FhirException) exception);
            issueType = fhirException.getIssueType();
            respStatus = fhirException.getResponseStatus();

            if (fhirException.hasIssueDetailConceptParts()) {
                Pair<String, Object[]> issueDetailConceptParts = fhirException.getIssueDetailConceptParts();
                String issueDetailConceptCode = issueDetailConceptParts.getKey();
                CodeableConcept baseIssueDetailConcept = this.vocabRepo.getValueSetConcepts().get(OP_OUTCOME_VALUE_SET_ID, issueDetailConceptCode);

                if (baseIssueDetailConcept != null) {
                    Coding baseIssueDetailConceptCoding = baseIssueDetailConcept.getCoding().get(0);

                    issueDetailConcept =
                        new CodeableConceptImpl().addCoding(new CodingImpl()
                            .setCode(baseIssueDetailConceptCoding.getCode())
                            .setDisplay(
                                new StringValueImpl().setValue(String.format(baseIssueDetailConceptCoding.getDisplay().getValue(),
                                    issueDetailConceptParts.getValue()))).setSystem(baseIssueDetailConceptCoding.getSystem())
                            .setVersion(baseIssueDetailConceptCoding.getVersion()));
                } else {
                    LOGGER.error(String.format("Unknown FHIR operation outcome issue detail concept (code=%s).", issueDetailConceptCode));
                }
            }
        }

        OperationOutcomeIssue opOutcomeIssue =
            new OperationOutcomeIssueImpl().setCode(new IssueTypeImpl().setValue(issueType)).setDetails(issueDetailConcept)
                .setSeverity(new IssueSeverityImpl().setValue(IssueSeverityList.ERROR));
        OperationOutcome opOutcome = new OperationOutcomeImpl().addIssue(opOutcomeIssue);

        if (MessageUtils.getContextualBoolean(JAXRSUtils.getCurrentMessage(), WsPropertyNames.ERROR_STACK_TRACE, false)) {
            // noinspection ThrowableResultOfMethodCallIgnored
            opOutcomeIssue.setDiagnostics(new StringValueImpl().setValue(SdcctExceptionUtils.getRootCause(exception).getMessage()));

            try {
                opOutcome.setText(new NarrativeImpl().setDiv(
                    new DivImpl().addContent(new String(this.xmlCodec.encode(new PreImpl().addContent(SdcctExceptionUtils.buildRootCauseStackTrace(exception)),
                        null), StandardCharsets.UTF_8))).setStatus(new NarrativeStatusImpl().setValue(NarrativeStatusList.GENERATED)));
            } catch (Exception e) {
                LOGGER.error("Unable to encode FHIR operation outcome narrative.", e);
            }
        }

        return Response.status(respStatus).entity(opOutcome).build();
    }

    public XmlCodec getXmlCodec() {
        return this.xmlCodec;
    }

    public void setXmlCodec(XmlCodec xmlCodec) {
        this.xmlCodec = xmlCodec;
    }
}
