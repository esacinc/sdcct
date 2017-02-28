package gov.hhs.onc.sdcct.testcases.submissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.hhs.onc.sdcct.json.impl.SdcctTestcaseDeserializer;
import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import javax.annotation.Nullable;

public interface SdcctTestcaseSubmission<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>> {
    public boolean hasEndpointAddress();

    @JsonProperty("endpointAddr")
    @Nullable
    public String getEndpointAddress();

    public void setEndpointAddress(@Nullable String endpointAddr);

    public boolean hasTestcase();

    @JsonDeserialize(using = SdcctTestcaseDeserializer.class)
    @JsonProperty
    @Nullable
    public U getTestcase();

    public void setTestcase(@Nullable U testcase);
}
