package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.logging.LoggingEvent;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringBuilder;
import org.slf4j.Marker;

public abstract class AbstractLoggingEvent implements LoggingEvent {
    @Override
    public Marker buildMarker() {
        return this.buildMarkerInternal().build();
    }

    protected MarkerBuilder buildMarkerInternal() {
        StringBuffer msgBuffer = new StringBuffer(), logstashFileMsgBuffer = new StringBuffer();
        SdcctToStringBuilder msgToStrBuilder = new SdcctToStringBuilder(msgBuffer),
            logstashFileMsgToStrBuilder = new SdcctToStringBuilder(logstashFileMsgBuffer);

        this.buildMarkerMessages(msgBuffer, msgToStrBuilder, logstashFileMsgBuffer, logstashFileMsgToStrBuilder);

        return new MarkerBuilder().appendField(this.buildMarkerFieldName(), this).appendMessage(msgBuffer.toString(), logstashFileMsgBuffer.toString());
    }

    protected abstract void buildMarkerMessages(StringBuffer msgBuffer, SdcctToStringBuilder msgToStrBuilder, StringBuffer logstashFileMsgBuffer,
        SdcctToStringBuilder logstashFileMsgToStrBuilder);

    protected abstract String buildMarkerFieldName();
}
