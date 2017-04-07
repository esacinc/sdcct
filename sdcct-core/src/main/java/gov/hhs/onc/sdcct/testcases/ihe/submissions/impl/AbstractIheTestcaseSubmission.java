package gov.hhs.onc.sdcct.testcases.ihe.submissions.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.submissions.impl.AbstractSdcctTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheTestcaseSubmission;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractIheTestcaseSubmission<T extends IheTestcase> extends AbstractSdcctTestcaseSubmission<IheTestcaseDescription, T>
    implements IheTestcaseSubmission<T> {
    protected String formId;

    protected AbstractIheTestcaseSubmission(@Nullable T testcase, @Nullable String endpointAddr, @Nullable String formId) {
        super(testcase, endpointAddr);

        this.formId = formId;
    }

    @Override
    public boolean hasFormId() {
        return !StringUtils.isBlank(this.formId);
    }

    @Override
    public String getFormId() {
        return this.formId;
    }

    @Override
    public void setFormId(String formId) {
        this.formId = formId;
    }
}
