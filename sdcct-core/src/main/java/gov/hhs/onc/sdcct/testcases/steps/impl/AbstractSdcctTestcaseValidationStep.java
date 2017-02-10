package gov.hhs.onc.sdcct.testcases.steps.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseValidationStep;

public class AbstractSdcctTestcaseValidationStep extends AbstractSdcctTestcaseStep implements SdcctTestcaseValidationStep {
    public AbstractSdcctTestcaseValidationStep(SpecificationType specType) {
        super(specType);
    }
}
