package gov.hhs.onc.sdcct.testcases.ihe.results;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheTestcaseSubmission;
import javax.annotation.Nullable;
import org.apache.cxf.interceptor.Fault;

public interface IheTestcaseResult<T extends IheTestcase, U extends IheTestcaseSubmission<T>> extends SdcctTestcaseResult<IheTestcaseDescription, T, U> {
    public boolean hasResponse();

    @Nullable
    public Object getResponse();

    public void setResponse(@Nullable Object response);

    public boolean hasFault();

    @Nullable
    public Fault getFault();

    public void setFault(@Nullable Fault fault);
}
