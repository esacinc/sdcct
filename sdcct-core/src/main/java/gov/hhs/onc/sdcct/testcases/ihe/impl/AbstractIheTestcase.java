package gov.hhs.onc.sdcct.testcases.ihe.impl;

import gov.hhs.onc.sdcct.rfd.ws.RfdWsResponseType;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseRequestInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseResponseInfo;
import gov.hhs.onc.sdcct.testcases.impl.AbstractSdcctTestcase;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

public abstract class AbstractIheTestcase extends AbstractSdcctTestcase<IheTestcaseDescription> implements IheTestcase {
    protected RfdWsResponseType contentType;
    protected QName op;
    protected IheTestcaseRequestInfo requestInfo;
    protected IheTestcaseResponseInfo responseInfo;

    @Override
    public RfdWsResponseType getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(RfdWsResponseType contentType) {
        this.contentType = contentType;
    }

    @Override
    public QName getOperation() {
        return this.op;
    }

    @Override
    public void setOperation(QName op) {
        this.op = op;
    }

    @Nullable
    @Override
    public IheTestcaseRequestInfo getRequestInfo() {
        return this.requestInfo;
    }

    @Override
    public void setRequestInfo(IheTestcaseRequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    @Nullable
    @Override
    public IheTestcaseResponseInfo getResponseInfo() {
        return this.responseInfo;
    }

    @Override
    public void setResponseInfo(IheTestcaseResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }
}
