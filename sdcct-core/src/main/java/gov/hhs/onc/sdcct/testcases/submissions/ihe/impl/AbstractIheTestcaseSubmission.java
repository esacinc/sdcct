package gov.hhs.onc.sdcct.testcases.submissions.ihe.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.submissions.impl.AbstractSdcctTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import javax.annotation.Nullable;

public abstract class AbstractIheTestcaseSubmission<T extends IheTestcase> extends AbstractSdcctTestcaseSubmission<IheTestcaseDescription, T>
    implements IheTestcaseSubmission<T> {
    protected AbstractIheTestcaseSubmission(@Nullable T testcase, @Nullable String endpointAddr) {
        super(testcase, endpointAddr);
    }
}
