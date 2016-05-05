package gov.hhs.onc.sdcct.data.history;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import java.util.Date;

public interface SdcctRevision extends SdcctEntity {
    public Date getTimestamp();

    public void setTimestamp(Date timestamp);
}
