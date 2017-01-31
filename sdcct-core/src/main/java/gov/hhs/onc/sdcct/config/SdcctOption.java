package gov.hhs.onc.sdcct.config;

import gov.hhs.onc.sdcct.beans.NamedBean;

public interface SdcctOption<T> extends NamedBean {
    public Class<T> getValueClass();
}
