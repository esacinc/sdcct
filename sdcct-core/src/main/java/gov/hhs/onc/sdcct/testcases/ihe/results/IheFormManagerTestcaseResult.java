package gov.hhs.onc.sdcct.testcases.ihe.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheFormManagerTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormManagerTestcaseResultImpl.class) })
public interface IheFormManagerTestcaseResult extends IheTestcaseResult<IheFormManagerTestcase, IheFormManagerTestcaseSubmission> {
}
