package gov.hhs.onc.sdcct.logging;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public enum AppenderType implements IdentifiedBean {
    CONSOLE, FILE, LOGSTASH_FILE;

    private final String id;

    private AppenderType() {
        this.id = SdcctStringUtils.joinCamelCase(SdcctStringUtils.splitCamelCase(this.name(), SdcctStringUtils.UNDERSCORE));
    }

    @Override
    public String getId() {
        return this.id;
    }
}
