package gov.hhs.onc.sdcct.testcases.ihe;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.ihe.impl.IheTestcaseDescriptionImpl;

@JsonSubTypes({ @Type(IheTestcaseDescriptionImpl.class) })
public interface IheTestcaseDescription extends SdcctTestcaseDescription {
}
