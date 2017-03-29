package gov.hhs.onc.sdcct.testcases.submissions.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class AbstractSdcctTestcaseSubmission<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>> implements SdcctTestcaseSubmission<T, U> {
    protected String endpointAddr;
    protected long submittedTimestamp;
    protected U testcase;
    protected String txId;

    protected AbstractSdcctTestcaseSubmission(@Nullable U testcase) {
        this.testcase = testcase;
    }

    protected AbstractSdcctTestcaseSubmission(@Nullable U testcase, @Nullable String endpointAddr) {
        this(testcase);

        this.endpointAddr = endpointAddr;
    }

    @Override
    public boolean hasEndpointAddress() {
        return this.endpointAddr != null;
    }

    @Nullable
    @Override
    public String getEndpointAddress() {
        return this.endpointAddr;
    }

    @Override
    public void setEndpointAddress(@Nullable String endpointAddr) {
        this.endpointAddr = endpointAddr;
    }

    @Nonnegative
    @Override
    public long getSubmittedTimestamp() {
        return this.submittedTimestamp;
    }

    @Override
    public void setSubmittedTimestamp(@Nonnegative long submittedTimestamp) {
        this.submittedTimestamp = submittedTimestamp;
    }

    @Override
    public boolean hasTestcase() {
        return this.testcase != null;
    }

    @Nullable
    @Override
    public U getTestcase() {
        return this.testcase;
    }

    @Override
    public void setTestcase(@Nullable U testcase) {
        this.testcase = testcase;
    }

    @Override
    public String getTxId() {
        return this.txId;
    }

    @Override
    public void setTxId(String txId) {
        this.txId = txId;
    }
}
