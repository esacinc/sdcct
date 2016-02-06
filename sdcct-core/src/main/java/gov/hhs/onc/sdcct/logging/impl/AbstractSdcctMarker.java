package gov.hhs.onc.sdcct.logging.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import net.logstash.logback.marker.LogstashMarker;

public abstract class AbstractSdcctMarker extends LogstashMarker {
    private final static long serialVersionUID = 0L;

    protected AbstractSdcctMarker(String nameSuffix) {
        super((MARKER_NAME_PREFIX + nameSuffix));
    }

    @Override
    public void writeTo(JsonGenerator jsonGen) throws IOException {
    }
}
