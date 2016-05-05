package gov.hhs.onc.sdcct.fhir.ws.impl;

import gov.hhs.onc.sdcct.fhir.CodeableConcept;
import gov.hhs.onc.sdcct.fhir.FhirFormException;
import gov.hhs.onc.sdcct.fhir.IssueSeverity;
import gov.hhs.onc.sdcct.fhir.IssueType;
import gov.hhs.onc.sdcct.fhir.NarrativeStatus;
import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeIssue;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeType;
import gov.hhs.onc.sdcct.fhir.impl.CodeTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodeableConceptImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodingImpl;
import gov.hhs.onc.sdcct.fhir.impl.IssueSeverityComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.IssueTypeComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeImpl;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeStatusComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.OperationOutcomeImpl;
import gov.hhs.onc.sdcct.fhir.impl.OperationOutcomeIssueImpl;
import gov.hhs.onc.sdcct.fhir.impl.StringTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.UriTypeImpl;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.DivImpl;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.PreImpl;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.nio.charset.StandardCharsets;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("provExceptionMapperFhir")
@Priority(0)
public class FhirExceptionMapper implements ExceptionMapper<Throwable> {
    private final static Logger LOGGER = LoggerFactory.getLogger(FhirExceptionMapper.class);

    @Autowired
    private XmlCodec xmlCodec;

    @Override
    public Response toResponse(Throwable exception) {
        IssueType issueType = IssueType.EXCEPTION;
        CodeableConcept issueDetailConcept = null;
        StatusType respStatus = Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof FhirFormException) {
            FhirFormException fhirFormException = ((FhirFormException) exception);
            issueType = fhirFormException.getIssueType();
            respStatus = fhirFormException.getResponseStatus();

            if (fhirFormException.hasOperationOutcomeType()) {
                OperationOutcomeType opOutcomeType = fhirFormException.getOperationOutcomeType();
                // noinspection ConstantConditions
                boolean opOutcomeDisplayNameAvailable = opOutcomeType.hasName();
                String opOutcomeName = null;

                if (opOutcomeDisplayNameAvailable) {
                    opOutcomeName = opOutcomeType.getName();

                    if (fhirFormException.hasOperationOutcomeArgs()) {
                        // noinspection ConstantConditions
                        opOutcomeName = String.format(opOutcomeName, fhirFormException.getOperationOutcomeArgs());
                    }
                }

                // noinspection ConstantConditions
                issueDetailConcept =
                    new CodeableConceptImpl().addCoding(new CodingImpl().setCode(new CodeTypeImpl().setValue(opOutcomeType.getId()))
                        .setDisplay((opOutcomeDisplayNameAvailable ? new StringTypeImpl().setValue(opOutcomeName) : null))
                        .setSystem(new UriTypeImpl().setValue(opOutcomeType.getCodeSystemUri().toString()))
                        .setVersion((opOutcomeType.hasCodeSystemVersion() ? new StringTypeImpl().setValue(opOutcomeType.getCodeSystemVersion()) : null)));
            }
        }

        OperationOutcomeIssue opOutcomeIssue =
            new OperationOutcomeIssueImpl().setCode(new IssueTypeComponentImpl().setValue(issueType)).setDetails(issueDetailConcept)
                .setSeverity(new IssueSeverityComponentImpl().setValue(IssueSeverity.ERROR));
        OperationOutcome opOutcome = new OperationOutcomeImpl().addIssue(opOutcomeIssue);

        if (MessageUtils.getContextualBoolean(JAXRSUtils.getCurrentMessage(), WsPropertyNames.ERROR_STACK_TRACE, false)) {
            // noinspection ThrowableResultOfMethodCallIgnored
            opOutcomeIssue.setDiagnostics(new StringTypeImpl().setValue(SdcctExceptionUtils.getRootCause(exception).getMessage()));

            try {
                opOutcome.setText(new NarrativeImpl().setDiv(
                    new DivImpl().addContent(new String(this.xmlCodec.encode(new PreImpl().addContent(SdcctExceptionUtils.buildRootCauseStackTrace(exception)),
                        null), StandardCharsets.UTF_8))).setStatus(new NarrativeStatusComponentImpl().setValue(NarrativeStatus.GENERATED)));
            } catch (Exception e) {
                LOGGER.error("Unable to encode FHIR operation outcome narrative.", e);
            }
        }

        return Response.status(respStatus).entity(opOutcome).build();
    }
}
