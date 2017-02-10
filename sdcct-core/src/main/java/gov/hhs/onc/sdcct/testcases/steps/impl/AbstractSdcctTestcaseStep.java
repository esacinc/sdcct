package gov.hhs.onc.sdcct.testcases.steps.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.beans.MessageBean;
import gov.hhs.onc.sdcct.beans.impl.AbstractResultBean;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStep;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStepDescription;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;

public abstract class AbstractSdcctTestcaseStep extends AbstractResultBean implements SdcctTestcaseStep {
    protected SdcctTestcaseStepDescription desc;
    protected List<MessageBean> execMsgs = new ArrayList<>();
    protected Boolean execSuccess;
    protected SpecificationType specType;

    public AbstractSdcctTestcaseStep(SpecificationType specType) {
        this.specType = specType;
    }

    @Override
    public List<MessageBean> getMessages() {
        return new ArrayList<>(this.execMsgs);
    }

    @Override
    public boolean isSuccess() {
        return this.isExecutionSuccess();
    }

    @Override
    public boolean hasDescription() {
        return this.desc != null;
    }

    @Nullable
    @Override
    public SdcctTestcaseStepDescription getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(@Nullable SdcctTestcaseStepDescription desc) {
        this.desc = desc;
    }

    @Override
    public boolean hasExecutionMessages() {
        return !this.execMsgs.isEmpty();
    }

    @Override
    public List<MessageBean> getExecutionMessages() {
        return this.execMsgs;
    }

    @Override
    public boolean isExecutionSuccess() {
        return ObjectUtils.defaultIfNull(this.execSuccess, true);
    }

    @Override
    public void setExecutionSuccess(boolean execSuccess) {
        this.execSuccess = execSuccess;
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
