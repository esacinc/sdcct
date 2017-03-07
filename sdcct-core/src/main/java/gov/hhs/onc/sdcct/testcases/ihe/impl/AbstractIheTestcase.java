package gov.hhs.onc.sdcct.testcases.ihe.impl;

import gov.hhs.onc.sdcct.rfd.ws.RfdWsResponseType;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseRequestInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseResponseInfo;
import gov.hhs.onc.sdcct.testcases.impl.AbstractSdcctTestcase;
import java.util.List;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

public abstract class AbstractIheTestcase extends AbstractSdcctTestcase<IheTestcaseDescription> implements IheTestcase {
    protected RfdWsResponseType contentType;
    protected List<String> formIds;
    protected IheTestcaseRequestInfo requestInfo;
    protected IheTestcaseResponseInfo responseInfo;
    protected QName transaction;

    @Override
    public RfdWsResponseType getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(RfdWsResponseType contentType) {
        this.contentType = contentType;
    }

    @Override
    public List<String> getFormIds() {
        return this.formIds;
    }

    @Override
    public void setFormIds(@Nullable List<String> formIds) {
        this.formIds = formIds;
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

    @Override
    public QName getTransaction() {
        return this.transaction;
    }

    @Override
    public void setTransaction(QName transaction) {
        this.transaction = transaction;
    }
}
