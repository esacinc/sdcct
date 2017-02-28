package gov.hhs.onc.sdcct.testcases.steps;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.beans.DescriptionBean;
import gov.hhs.onc.sdcct.testcases.steps.impl.SdcctTestcaseStepDescriptionImpl;

@JsonSubTypes({ @Type(SdcctTestcaseStepDescriptionImpl.class) })
public interface SdcctTestcaseStepDescription extends DescriptionBean {
    public void setText(String text);
}
