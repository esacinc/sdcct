package gov.hhs.onc.sdcct.testcases.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import java.util.List;
import javax.annotation.Nullable;

public abstract class AbstractSdcctTestcaseDescription implements SdcctTestcaseDescription {
    protected String instructions;
    protected List<String> specifications;
    protected String text;

    @Override
    public boolean hasInstructions() {
        return this.instructions != null;
    }

    @Nullable
    @Override
    public String getInstructions() {
        return this.instructions;
    }

    @Override
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean hasSpecifications() {
        return this.specifications != null;
    }

    @Nullable
    @Override
    public List<String> getSpecifications() {
        return this.specifications;
    }

    @Override
    public void setSpecifications(@Nullable List<String> specifications) {
        this.specifications = specifications;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
