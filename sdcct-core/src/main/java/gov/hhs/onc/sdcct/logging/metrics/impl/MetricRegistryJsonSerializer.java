package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import gov.hhs.onc.sdcct.logging.utils.SdcctMarkerUtils;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component("jsonSerializerMetricRegistry")
public class MetricRegistryJsonSerializer extends StdSerializer<MetricRegistry> {
    private final static long serialVersionUID = 0L;

    public MetricRegistryJsonSerializer() {
        super(MetricRegistry.class);
    }

    @Override
    public void serialize(MetricRegistry metricRegistry, JsonGenerator jsonGen, SerializerProvider serializerProv) throws IOException {
        jsonGen.writeStartObject();

        SdcctMarkerUtils.serializeFields(jsonGen, metricRegistry.getMetrics(),
            metric -> ((metric instanceof Gauge<?>) ? ((Gauge<?>) metric).getValue() : metric));

        jsonGen.writeEndObject();
    }
}
