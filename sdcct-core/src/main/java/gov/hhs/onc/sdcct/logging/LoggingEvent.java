package gov.hhs.onc.sdcct.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import javax.annotation.Nullable;

public interface LoggingEvent extends IdentifiedBean {
    @JsonProperty("eventId")
    @Nullable
    @Override
    public String getId();
}
