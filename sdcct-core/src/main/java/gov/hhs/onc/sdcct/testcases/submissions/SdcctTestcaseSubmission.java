package gov.hhs.onc.sdcct.testcases.submissions;

import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import javax.annotation.Nullable;

public interface SdcctTestcaseSubmission<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>> {
    public boolean hasEndpointAddress();

    @Nullable
    public String getEndpointAddress();

    public void setEndpointAddress(@Nullable String endpointAddr);

    public boolean hasTestcase();

    @Nullable
    public U getTestcase();

    public void setTestcase(@Nullable U testcase);
}
