package gov.hhs.onc.sdcct.testcases.ihe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.testcases.ihe.impl.IheFormManagerTestcaseImpl;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(IheFormManagerTestcaseImpl.class) })
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
