package gov.hhs.onc.sdcct.testcases.ihe.results;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheFormArchiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormArchiverTestcaseResultImpl.class) })
public interface IheFormArchiverTestcaseResult extends IheTestcaseResult<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission> {
}
