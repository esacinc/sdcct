package gov.hhs.onc.sdcct.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface DescriptionBean {
    @JsonProperty
    public String getText();
}
