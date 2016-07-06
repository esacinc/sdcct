package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.api.SdcctIssue;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import javax.annotation.Nullable;

public interface FhirIssue extends SdcctIssue {
    public boolean hasDetailType();

    @Nullable
    public OperationOutcomeType getDetailType();

    @Override
    public FhirIssue setLocation(@Nullable SdcctLocation loc);

    @Override
    public FhirIssue setMessage(@Nullable String msg);

    @Override
    public FhirIssue setSource(@Nullable String src);

    public IssueType getType();
}
