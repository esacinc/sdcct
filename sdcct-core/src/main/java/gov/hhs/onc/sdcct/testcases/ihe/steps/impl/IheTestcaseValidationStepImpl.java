package gov.hhs.onc.sdcct.testcases.ihe.steps.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.testcases.steps.impl.AbstractSdcctTestcaseValidationStep;
import gov.hhs.onc.sdcct.testcases.ihe.steps.IheTestcaseValidationStep;

public class IheTestcaseValidationStepImpl extends AbstractSdcctTestcaseValidationStep implements IheTestcaseValidationStep {
    public IheTestcaseValidationStepImpl() {
        super(SpecificationType.RFD);
    }
}
