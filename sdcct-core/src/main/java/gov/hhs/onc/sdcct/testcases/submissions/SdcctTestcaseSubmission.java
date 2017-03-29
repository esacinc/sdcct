package gov.hhs.onc.sdcct.testcases.submissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.hhs.onc.sdcct.json.impl.SdcctTestcaseDeserializer;
import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.hibernate.validator.constraints.NotEmpty;

public interface SdcctTestcaseSubmission<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>> {
    public boolean hasEndpointAddress();

    // TODO: update to include custom message
    @JsonProperty("endpointAddr")
    @Nullable
    @NotEmpty
    public String getEndpointAddress();

    public void setEndpointAddress(@Nullable String endpointAddr);

    @JsonProperty
    @Nonnegative
    public long getSubmittedTimestamp();

    public void setSubmittedTimestamp(@Nonnegative long submittedTimestamp);

    public boolean hasTestcase();

    @JsonDeserialize(using = SdcctTestcaseDeserializer.class)
    @JsonProperty
    @Nullable
    public U getTestcase();

    public void setTestcase(@Nullable U testcase);

    @JsonProperty
    public String getTxId();

    public void setTxId(String txId);
}
