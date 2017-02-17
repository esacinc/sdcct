package gov.hhs.onc.sdcct.testcases.steps.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.beans.MessageBean;
import gov.hhs.onc.sdcct.beans.impl.AbstractResultBean;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStep;
import gov.hhs.onc.sdcct.testcases.steps.SdcctTestcaseStepDescription;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;

public abstract class AbstractSdcctTestcaseStep extends AbstractResultBean implements SdcctTestcaseStep {
    protected SdcctTestcaseStepDescription desc;
    protected ListMultimap<SdcctIssueSeverity, MessageBean> execMsgs = ArrayListMultimap.create();
    protected Boolean execSuccess;
    protected SpecificationType specType;

    public AbstractSdcctTestcaseStep(SpecificationType specType) {
        this.specType = specType;
    }

    @Override
    public ListMultimap<SdcctIssueSeverity, MessageBean> getMessages() {
        return ArrayListMultimap.create(this.execMsgs);
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
    public ListMultimap<SdcctIssueSeverity, MessageBean> getExecutionMessages() {
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
