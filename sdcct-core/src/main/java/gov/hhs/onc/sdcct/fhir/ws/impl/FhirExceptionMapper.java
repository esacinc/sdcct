package gov.hhs.onc.sdcct.fhir.ws.impl;

import gov.hhs.onc.sdcct.api.ApiException;
import gov.hhs.onc.sdcct.api.SdcctIssue;
import gov.hhs.onc.sdcct.fhir.FhirIssue;
import gov.hhs.onc.sdcct.fhir.IssueSeverity;
import gov.hhs.onc.sdcct.fhir.IssueType;
import gov.hhs.onc.sdcct.fhir.NarrativeStatus;
import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeIssue;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeType;
import gov.hhs.onc.sdcct.fhir.impl.CodeTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodeableConceptImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodingImpl;
import gov.hhs.onc.sdcct.fhir.impl.ExtensionImpl;
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
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.transform.content.path.ContentPath;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import java.io.Serializable;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.MessageUtils;
import org.springframework.stereotype.Component;

@Component("provExceptionMapperFhir")
@Priority(0)
public class FhirExceptionMapper implements ExceptionMapper<Throwable> {
    private final static String OP_OUTCOME_ISSUE_SRC_EXT_URL_VALUE = (SdcctUris.FHIR_URL_VALUE + "/StructureDefinition/operationoutcome-issue-source");

    @Override
    public Response toResponse(Throwable exception) {
        OperationOutcome opOutcome = new OperationOutcomeImpl();
        StatusType respStatus = Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof ApiException) {
            ApiException apiException = ((ApiException) exception);

            respStatus = apiException.getResponseStatus();

            if (apiException.hasIssues()) {
                OperationOutcomeIssue opOutcomeIssue;
                IssueType issueType = IssueType.EXCEPTION;
                FhirIssue fhirIssue;
                OperationOutcomeType issueDetailType;
                SdcctLocation issueLoc;
                ContentPath issueLocPath;

                for (SdcctIssue issue : apiException.getIssues()) {
                    opOutcome.addIssue((opOutcomeIssue = new OperationOutcomeIssueImpl()
                        .setSeverity(new IssueSeverityComponentImpl().setValue(SdcctEnumUtils.findById(IssueSeverity.class, issue.getLevel().getId())))));

                    if (issue instanceof FhirIssue) {
                        issueType = (fhirIssue = ((FhirIssue) issue)).getType();

                        if (fhirIssue.hasDetailType()) {
                            // noinspection ConstantConditions
                            opOutcomeIssue.setDetails(new CodeableConceptImpl().addCoding(new CodingImpl()
                                .setCode(new CodeTypeImpl().setValue((issueDetailType = fhirIssue.getDetailType()).getId()))
                                .setSystem(new UriTypeImpl().setValue(issueDetailType.getCodeSystemUri().toString())).setVersion(
                                    (issueDetailType.hasCodeSystemVersion() ? new StringTypeImpl().setValue(issueDetailType.getCodeSystemVersion()) : null))));
                        }
                    }

                    opOutcomeIssue.setCode(new IssueTypeComponentImpl().setValue(issueType));

                    if (issue.hasMessage()) {
                        opOutcomeIssue.setDiagnostics(new StringTypeImpl().setValue(issue.getMessage()));
                    }

                    // noinspection ConstantConditions
                    if (issue.hasLocation() && (issueLoc = issue.getLocation()).hasContentPath()) {
                        // noinspection ConstantConditions
                        opOutcomeIssue.addExpressions(new StringTypeImpl().setValue((issueLocPath = issueLoc.getContentPath()).getFluentPathExpression()));
                        opOutcomeIssue.addLocations(new StringTypeImpl().setValue(issueLocPath.getXpathExpression()));
                    }

                    if (issue.hasSource()) {
                        opOutcomeIssue.addExtensions(
                            new ExtensionImpl().setUrl(OP_OUTCOME_ISSUE_SRC_EXT_URL_VALUE).setContent(new StringTypeImpl().setValue(issue.getSource())));
                    }
                }
            }

            if (MessageUtils.getContextualBoolean(JAXRSUtils.getCurrentMessage(), WsPropertyNames.ERROR_STACK_TRACE, false)) {
                opOutcome.setText(new NarrativeImpl()
                    .setDiv(new DivImpl().addContent(((Serializable) new PreImpl().addContent(SdcctExceptionUtils.buildRootCauseStackTrace(exception)))))
                    .setStatus(new NarrativeStatusComponentImpl().setValue(NarrativeStatus.GENERATED)));
            }
        }

        return Response.status(respStatus).entity(opOutcome).build();
    }
}
