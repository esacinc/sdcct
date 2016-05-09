package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import org.apache.commons.lang3.StringUtils;

public enum DbStatementType implements IdentifiedBean {
    STATEMENT, PREPARED_STATEMENT, CALLABLE_STATEMENT;

    private final String id;

    private DbStatementType() {
        this.id = StringUtils.join(StringUtils.split(this.name().toLowerCase(), SdcctStringUtils.UNDERSCORE, 2), StringUtils.SPACE);
    }

    @Override
    public String getId() {
        return this.id;
    }
}
