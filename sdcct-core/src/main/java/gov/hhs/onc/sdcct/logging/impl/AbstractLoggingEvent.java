package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.logging.LoggingEvent;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Marker;

public abstract class AbstractLoggingEvent implements LoggingEvent {
    @Override
    public Marker buildMarker() {
        return this.buildMarkerInternal().build();
    }

    protected MarkerBuilder buildMarkerInternal() {
        StringBuffer msgBuffer = new StringBuffer(), logstashFileMsgBuffer = new StringBuffer();
        ToStringBuilder msgToStrBuilder = new ToStringBuilder(null, SdcctToStringStyle.INSTANCE, msgBuffer),
            logstashFileMsgToStrBuilder = new ToStringBuilder(null, SdcctToStringStyle.INSTANCE, logstashFileMsgBuffer);

        this.buildMarkerMessages(msgBuffer, msgToStrBuilder, logstashFileMsgBuffer, logstashFileMsgToStrBuilder);

        return new MarkerBuilder().appendField(this.buildMarkerFieldName(), this).appendMessage(msgBuffer.toString(), logstashFileMsgBuffer.toString());
    }

    protected abstract void buildMarkerMessages(StringBuffer msgBuffer, ToStringBuilder msgToStrBuilder, StringBuffer logstashFileMsgBuffer,
        ToStringBuilder logstashFileMsgToStrBuilder);

    protected abstract String buildMarkerFieldName();
}
