package gov.hhs.onc.sdcct.testcases.ihe;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import javax.annotation.Nullable;

public interface IheFormManagerTestcase extends IheTestcase {
    @JsonProperty
    public RetrieveFormRequestType getRequestParams();

    public void setRequestParams(RetrieveFormRequestType requestParams);

    public boolean hasResponse();

    @JsonProperty
    @Nullable
    public RetrieveFormResponseType getResponse();

    public void setResponse(@Nullable RetrieveFormResponseType response);
}
