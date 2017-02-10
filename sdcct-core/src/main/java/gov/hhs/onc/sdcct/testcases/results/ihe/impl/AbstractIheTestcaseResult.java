package gov.hhs.onc.sdcct.testcases.results.ihe.impl;

import gov.hhs.onc.sdcct.testcases.results.impl.AbstractSdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import javax.annotation.Nullable;
import org.apache.cxf.interceptor.Fault;

public abstract class AbstractIheTestcaseResult<T extends IheTestcase, U extends IheTestcaseSubmission<T>>
    extends AbstractSdcctTestcaseResult<IheTestcaseDescription, T, U> implements IheTestcaseResult<T, U> {
    protected Object response;
    protected Fault fault;

    protected AbstractIheTestcaseResult(U submission) {
        super(submission);
    }

    @Override
    public boolean hasResponse() {
        return this.response != null;
    }

    @Nullable
    @Override
    public Object getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(@Nullable Object response) {
        this.response = response;
    }

    @Override
    public boolean hasFault() {
        return this.fault != null;
    }

    @Nullable
    @Override
    public Fault getFault() {
        return this.fault;
    }

    @Override
    public void setFault(@Nullable Fault fault) {
        this.fault = fault;
    }
}
