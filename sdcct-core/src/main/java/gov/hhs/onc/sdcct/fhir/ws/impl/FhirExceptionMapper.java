package gov.hhs.onc.sdcct.fhir.ws.impl;

import gov.hhs.onc.sdcct.fhir.IssueSeverity;
import gov.hhs.onc.sdcct.fhir.IssueType;
import gov.hhs.onc.sdcct.fhir.NarrativeStatus;
import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeType;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeImpl;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeStatusComponentImpl;
import gov.hhs.onc.sdcct.fhir.ws.FhirWsException;
import gov.hhs.onc.sdcct.fhir.ws.utils.SdcctFhirOperationOutcomeUtils.OperationOutcomeBuilder;
import gov.hhs.onc.sdcct.fhir.ws.utils.SdcctFhirOperationOutcomeUtils.OperationOutcomeIssueBuilder;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.DivImpl;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.PreImpl;
import gov.hhs.onc.sdcct.net.http.SdcctHttpStatus;
import gov.hhs.onc.sdcct.transform.content.path.impl.ContentPathImpl;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringBuilder;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.validate.ValidationLocation;
import gov.hhs.onc.sdcct.validate.ValidationSource;
import gov.hhs.onc.sdcct.validate.ValidationType;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.commons.lang3.ArrayUtils;
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
        SdcctHttpStatus respStatus = SdcctHttpStatus.INTERNAL_SERVER_ERROR;
        OperationOutcome opOutcome = null;

        if (exception instanceof FhirWsException) {
            FhirWsException wsException = ((FhirWsException) exception);

            respStatus = wsException.getResponseStatus();

            if (wsException.hasOperationOutcome()) {
                opOutcome = wsException.getOperationOutcome();
            }
        }

        if (opOutcome == null) {
            opOutcome = new OperationOutcomeBuilder().build();
        }

        // noinspection ThrowableResultOfMethodCallIgnored
        ValidationException validationCause = SdcctExceptionUtils.findCause(exception, ValidationException.class);

        if (validationCause != null) {
            for (ValidationIssue validationIssue : validationCause.getResult().getIssues().getIssues()) {
                opOutcome.addIssues(buildValidationIssueSource(buildValidationIssueDetails(new OperationOutcomeIssueBuilder()
                    .setType(buildValidationIssueType(validationIssue.getType())).setSeverity(IssueSeverity.valueOf(validationIssue.getSeverity().name()))
                    .setLocation(buildValidationLocation(validationIssue.getLocation()))
                    .setDetails(OperationOutcomeType.MSG_ERROR_PARSING, validationIssue.getMessage()), validationIssue), validationIssue).build());
            }
        }

        if (!opOutcome.hasIssues()) {
            opOutcome.addIssues(new OperationOutcomeIssueBuilder().build());
        }

        if (MessageUtils.getContextualBoolean(JAXRSUtils.getCurrentMessage(), WsPropertyNames.ERROR_STACK_TRACE, false)) {
            try {
                opOutcome.setText(new NarrativeImpl()
                    .setDiv(new DivImpl().addContent(new String(
                        this.xmlCodec.encode(new PreImpl().addContent(SdcctExceptionUtils.buildRootCauseStackTrace(exception)), null), StandardCharsets.UTF_8)))
                    .setStatus(new NarrativeStatusComponentImpl().setValue(NarrativeStatus.ADDITIONAL)));
            } catch (Exception e) {
                LOGGER.error("Unable to encode FHIR operation outcome narrative.", e);
            }
        }

        return Response.status(respStatus).entity(opOutcome).build();
    }

    private static OperationOutcomeIssueBuilder buildValidationIssueSource(OperationOutcomeIssueBuilder opOutcomeIssueBuilder,
        ValidationIssue validationIssue) {
        if (!validationIssue.hasSource()) {
            return opOutcomeIssueBuilder;
        }

        ValidationSource validationSrc = validationIssue.getSource();

        SdcctToStringBuilder validationSrcBuilder = new SdcctToStringBuilder();
        validationSrcBuilder.append("id", validationSrc.getId());
        validationSrcBuilder.append("name", validationSrc.getName());

        if (validationSrc.hasUri()) {
            validationSrcBuilder.append("uri", validationSrc.getUri());
        }

        return opOutcomeIssueBuilder.setSource(validationSrcBuilder.build());
    }

    private static OperationOutcomeIssueBuilder buildValidationIssueDetails(OperationOutcomeIssueBuilder opOutcomeIssueBuilder,
        ValidationIssue validationIssue) {
        OperationOutcomeType detailType;
        Object[] detailArgs;

        // TODO: Improve validation issue details.
        switch (validationIssue.getType()) {
            default:
                detailType = OperationOutcomeType.MSG_ERROR_PARSING;
                detailArgs = ArrayUtils.toArray(validationIssue.getMessage());
                break;
        }

        return opOutcomeIssueBuilder.setDetails(detailType, detailArgs);
    }

    private static SdcctLocation buildValidationLocation(ValidationLocation validationLoc) {
        SdcctLocation loc = new SdcctLocation(null, validationLoc.getLineNumber(), validationLoc.getColumnNumber());
        loc.setContentPath(new ContentPathImpl(Collections.emptyMap(), new LinkedList<>(), validationLoc.getFluentPathExpression(),
            validationLoc.getJsonPointerExpression(), validationLoc.getXpathExpression()));

        return loc;
    }

    private static IssueType buildValidationIssueType(ValidationType validationType) {
        switch (validationType) {
            case SCHEMA:
                return IssueType.STRUCTURE;

            case SCHEMATRON:
                return IssueType.INVARIANT;

            default:
                return IssueType.CODE_INVALID;
        }
    }
}
