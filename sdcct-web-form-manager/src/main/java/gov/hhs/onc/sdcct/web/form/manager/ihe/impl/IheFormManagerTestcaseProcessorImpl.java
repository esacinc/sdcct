package gov.hhs.onc.sdcct.web.form.manager.ihe.impl;

import gov.hhs.onc.sdcct.api.RoleNames;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.AbstractIheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormManagerTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.form.manager.ihe.IheFormManagerTestcaseProcessor;

public class IheFormManagerTestcaseProcessorImpl
    extends AbstractIheTestcaseProcessor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult, RetrieveFormRequestType>
    implements IheFormManagerTestcaseProcessor {
    public IheFormManagerTestcaseProcessorImpl(String clientBeanName) {
        super(clientBeanName, SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT, RoleNames.FORM_MANAGER,
            SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_SUBMISSION, IheFormManagerTestcaseResultImpl.class);
    }

    @Override
    protected RetrieveFormRequestType createRequestInternal(IheFormManagerTestcaseSubmission submission) throws Exception {
        IheFormManagerTestcase testcase = submission.getTestcase();
        // noinspection ConstantConditions
        RetrieveFormRequestType retrieveFormRequestType = testcase.getRequestParams();

        if (retrieveFormRequestType.getWorkflowData().hasFormId()) {
            retrieveFormRequestType.getWorkflowData().setFormId(submission.getFormId());
        }

        return retrieveFormRequestType;
    }
}
