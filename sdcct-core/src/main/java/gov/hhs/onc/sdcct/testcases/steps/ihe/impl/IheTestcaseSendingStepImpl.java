package gov.hhs.onc.sdcct.testcases.steps.ihe.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.testcases.steps.impl.AbstractSdcctTestcaseSendingStep;
import gov.hhs.onc.sdcct.testcases.steps.ihe.IheTestcaseSendingStep;

public class IheTestcaseSendingStepImpl extends AbstractSdcctTestcaseSendingStep implements IheTestcaseSendingStep {
    public IheTestcaseSendingStepImpl() {
        super(SpecificationType.RFD);
    }
}
