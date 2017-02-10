package gov.hhs.onc.sdcct.testcases.steps.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStep;

public abstract class AbstractSdcctTestcaseSendingStep extends AbstractSdcctTestcaseStep implements SdcctTestcaseStep {
    public AbstractSdcctTestcaseSendingStep(SpecificationType specType) {
        super(specType);
    }
}
