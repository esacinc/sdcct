package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.api.impl.SdcctIssueImpl;
import gov.hhs.onc.sdcct.fhir.FhirIssue;
import gov.hhs.onc.sdcct.fhir.IssueType;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeType;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import javax.annotation.Nullable;

public class FhirIssueImpl extends SdcctIssueImpl implements FhirIssue {
    private IssueType type;
    private OperationOutcomeType detailType;

    public FhirIssueImpl(IssueLevel level, IssueType type) {
        this(level, type, null);
    }

    public FhirIssueImpl(IssueLevel level, IssueType type, @Nullable OperationOutcomeType detailType, Object ... detailArgs) {
        super(level);

        this.type = type;
        this.detailType = detailType;

        if (this.hasDetailType()) {
            // noinspection ConstantConditions
            this.msg = String.format(detailType.getName(), detailArgs);
        }
    }

    @Override
    public boolean hasDetailType() {
        return (this.detailType != null);
    }

    @Nullable
    @Override
    public OperationOutcomeType getDetailType() {
        return this.detailType;
    }

    @Override
    public FhirIssue setLocation(@Nullable SdcctLocation loc) {
        return ((FhirIssue) super.setLocation(loc));
    }

    @Override
    public FhirIssue setMessage(@Nullable String msg) {
        return ((FhirIssue) super.setMessage(msg));
    }

    @Override
    public FhirIssue setSource(@Nullable String src) {
        return ((FhirIssue) super.setSource(src));
    }

    @Override
    public IssueType getType() {
        return this.type;
    }
}
