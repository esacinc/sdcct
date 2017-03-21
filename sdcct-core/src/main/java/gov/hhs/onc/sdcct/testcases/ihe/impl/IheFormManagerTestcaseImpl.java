package gov.hhs.onc.sdcct.testcases.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections.CollectionUtils;

@JsonTypeName("iheFormManagerTestcase")
public class IheFormManagerTestcaseImpl extends AbstractIheTestcase implements IheFormManagerTestcase {
    private List<String> formIds;
    private RetrieveFormRequestType requestParams;
    private RetrieveFormResponseType response;

    @Override
    public boolean hasFormIds() {
        return !CollectionUtils.isEmpty(this.formIds);
    }

    @Nullable
    @Override
    public List<String> getFormIds() {
        return this.formIds;
    }

    @Override
    public void setFormIds(@Nullable List<String> formIds) {
        this.formIds = formIds;
    }

    @Override
    public RetrieveFormRequestType getRequestParams() {
        return this.requestParams;
    }

    @Override
    public void setRequestParams(RetrieveFormRequestType requestParams) {
        this.requestParams = requestParams;
    }

    @Override
    public boolean hasResponse() {
        return this.response != null;
    }

    @Nullable
    @Override
    public RetrieveFormResponseType getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(@Nullable RetrieveFormResponseType response) {
        this.response = response;
    }
}
