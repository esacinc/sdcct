package gov.hhs.onc.sdcct.testcases.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.SpecificationRole;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStep;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSdcctTestcase<T extends SdcctTestcaseDescription> implements SdcctTestcase<T> {
    protected T desc;
    protected String id;
    protected String name;
    protected boolean neg;
    protected boolean optional;
    protected SpecificationRole roleTested;
    protected boolean sdcctInitiated;
    protected SpecificationRole sdcctRole;
    protected List<SdcctTestcaseStep> steps;

    @Override
    public boolean hasDescription() {
        return this.desc != null;
    }

    @Nullable
    @Override
    public T getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(@Nullable T desc) {
        this.desc = desc;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    public boolean isNegative() {
        return this.neg;
    }

    @Override
    public void setNegative(boolean neg) {
        this.neg = neg;
    }

    @Override
    public boolean isOptional() {
        return this.optional;
    }

    @Override
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    @Override
    public SpecificationRole getRoleTested() {
        return this.roleTested;
    }

    @Override
    public void setRoleTested(SpecificationRole roleTested) {
        this.roleTested = roleTested;
    }

    @Override
    public boolean isSdcctInitiated() {
        return this.sdcctInitiated;
    }

    @Override
    public void setSdcctInitiated(boolean sdcctInitiated) {
        this.sdcctInitiated = sdcctInitiated;
    }

    @Override
    public SpecificationRole getSdcctRole() {
        return this.sdcctRole;
    }

    @Override
    public void setSdcctRole(SpecificationRole sdcctRole) {
        this.sdcctRole = sdcctRole;
    }

    @Override
    public boolean hasSteps() {
        return !this.steps.isEmpty();
    }

    @Override
    public List<SdcctTestcaseStep> getSteps() {
        return this.steps;
    }

    @Override
    public void setSteps(List<SdcctTestcaseStep> steps) {
        this.steps = steps;
    }
}
