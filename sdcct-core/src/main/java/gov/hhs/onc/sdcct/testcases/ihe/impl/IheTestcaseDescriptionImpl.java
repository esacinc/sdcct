package gov.hhs.onc.sdcct.testcases.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.impl.AbstractSdcctTestcaseDescription;

@JsonTypeName("iheTestcaseDesc")
public class IheTestcaseDescriptionImpl extends AbstractSdcctTestcaseDescription implements IheTestcaseDescription {
}
