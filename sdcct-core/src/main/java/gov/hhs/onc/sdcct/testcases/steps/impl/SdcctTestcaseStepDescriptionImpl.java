package gov.hhs.onc.sdcct.testcases.steps.impl;

import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStepDescription;

public class SdcctTestcaseStepDescriptionImpl implements SdcctTestcaseStepDescription {
    private String text;

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
