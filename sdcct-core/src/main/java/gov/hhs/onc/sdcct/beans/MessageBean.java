package gov.hhs.onc.sdcct.beans;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;

public interface MessageBean extends DescriptionBean {
    public SdcctIssueSeverity getSeverity();
}
