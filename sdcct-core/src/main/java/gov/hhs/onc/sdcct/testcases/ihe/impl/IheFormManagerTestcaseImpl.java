package gov.hhs.onc.sdcct.testcases.ihe.impl;

import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import javax.annotation.Nullable;

public class IheFormManagerTestcaseImpl extends AbstractIheTestcase implements IheFormManagerTestcase {
    private RetrieveFormRequestType requestParams;
    private RetrieveFormResponseType response;

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
