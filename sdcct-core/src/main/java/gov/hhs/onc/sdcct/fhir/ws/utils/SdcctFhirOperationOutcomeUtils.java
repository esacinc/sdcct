package gov.hhs.onc.sdcct.fhir.ws.utils;

import gov.hhs.onc.sdcct.fhir.IssueSeverity;
import gov.hhs.onc.sdcct.fhir.IssueType;
import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeIssue;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeType;
import gov.hhs.onc.sdcct.fhir.impl.CodeTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodeableConceptImpl;
import gov.hhs.onc.sdcct.fhir.impl.CodingImpl;
import gov.hhs.onc.sdcct.fhir.impl.ExtensionImpl;
import gov.hhs.onc.sdcct.fhir.impl.IssueSeverityComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.IssueTypeComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.OperationOutcomeImpl;
import gov.hhs.onc.sdcct.fhir.impl.OperationOutcomeIssueImpl;
import gov.hhs.onc.sdcct.fhir.impl.StringTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.UriTypeImpl;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.transform.content.path.ContentPath;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.Builder;

public final class SdcctFhirOperationOutcomeUtils {
    public static class OperationOutcomeIssueBuilder implements Builder<OperationOutcomeIssue> {
        private final static String OP_OUTCOME_ISSUE_SRC_EXT_URL_VALUE = (SdcctUris.FHIR_URL_VALUE + "/StructureDefinition/operationoutcome-issue-source");

        private Object[] detailArgs = ArrayUtils.EMPTY_OBJECT_ARRAY;
        private OperationOutcomeType detailType;
        private String diagnostics;
        private SdcctLocation loc;
        private IssueSeverity severity = IssueSeverity.ERROR;
        private String src;
        private IssueType type = IssueType.EXCEPTION;

        @Override
        public OperationOutcomeIssue build() {
            OperationOutcomeIssue issue = new OperationOutcomeIssueImpl().setCode(new IssueTypeComponentImpl().setValue(this.type))
                .setSeverity(new IssueSeverityComponentImpl().setValue(this.severity));

            if (this.detailType != null) {
                // noinspection ConstantConditions
                issue.setDetails(new CodeableConceptImpl().addCoding(new CodingImpl().setCode(new CodeTypeImpl().setValue(this.detailType.getId()))
                    .setDisplay(new StringTypeImpl().setValue(String.format(this.detailType.getName(), this.detailArgs)))
                    .setSystem(new UriTypeImpl().setValue(this.detailType.getCodeSystemUri().toString()))
                    .setVersion((this.detailType.hasCodeSystemVersion() ? new StringTypeImpl().setValue(this.detailType.getCodeSystemVersion()) : null))));
            }

            if (this.diagnostics != null) {
                issue.setDiagnostics(new StringTypeImpl().setValue(this.diagnostics));
            }

            if ((this.loc != null) && (this.loc.hasContentPath())) {
                ContentPath locContentPath = this.loc.getContentPath();
                // noinspection ConstantConditions
                issue.addExpressions(new StringTypeImpl().setValue(locContentPath.getFluentPathExpression()));
                issue.addLocations(new StringTypeImpl().setValue(locContentPath.getXpathExpression()));
            }

            if (this.src != null) {
                issue.addExtensions(new ExtensionImpl().setUrl(OP_OUTCOME_ISSUE_SRC_EXT_URL_VALUE).setValue(new StringTypeImpl().setValue(this.src)));
            }

            return issue;
        }

        public OperationOutcomeIssueBuilder setDetails(OperationOutcomeType detailType, Object ... detailArgs) {
            this.detailType = detailType;
            this.detailArgs = detailArgs;

            return this;
        }

        public OperationOutcomeIssueBuilder setDiagnostics(String diagnostics) {
            this.diagnostics = diagnostics;

            return this;
        }

        public OperationOutcomeIssueBuilder setLocation(SdcctLocation loc) {
            this.loc = loc;

            return this;
        }

        public OperationOutcomeIssueBuilder setSeverity(IssueSeverity severity) {
            this.severity = severity;

            return this;
        }

        public OperationOutcomeIssueBuilder setSource(String src) {
            this.src = src;

            return this;
        }

        public OperationOutcomeIssueBuilder setType(IssueType type) {
            this.type = type;

            return this;
        }
    }

    public static class OperationOutcomeBuilder implements Builder<OperationOutcome> {
        private List<OperationOutcomeIssue> issues = new ArrayList<>();

        @Override
        public OperationOutcome build() {
            OperationOutcome outcome = new OperationOutcomeImpl();

            if (this.issues.isEmpty()) {
                this.issues.add(new OperationOutcomeIssueBuilder().build());
            }

            outcome.getIssues().addAll(this.issues);

            return outcome;
        }

        public OperationOutcomeBuilder addIssues(OperationOutcomeIssue ... issues) {
            Stream.of(issues).forEach(this.issues::add);

            return this;
        }
    }

    private SdcctFhirOperationOutcomeUtils() {
    }
}
