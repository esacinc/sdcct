package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import gov.hhs.onc.sdcct.logging.utils.SdcctMarkerUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
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

        serializeMetricField(jsonGen, null, metricRegistry.getMetrics());

        jsonGen.writeEndObject();
    }

    private static void serializeMetricField(JsonGenerator jsonGen, @Nullable String metricFieldName, Map<String, ?> metricMap) throws IOException {
        boolean metricFieldNameAvailable = (metricFieldName != null);

        if (metricFieldNameAvailable) {
            jsonGen.writeObjectFieldStart(SdcctMarkerUtils.buildFieldName(metricFieldName));
        }

        if (!metricMap.isEmpty()) {
            Map<String, List<String>> metricKeyMap = metricMap.keySet().stream().sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.groupingBy((metricKey) -> StringUtils.split(metricKey, SdcctStringUtils.PERIOD, 2)[0]));

            List<String> metricKeys;
            String metricFieldKey;
            Object metricFieldValue;

            for (String metricKeyPrefix : metricKeyMap.keySet()) {
                if (((metricKeys = metricKeyMap.get(metricKeyPrefix)).size() == 1)
                    && !StringUtils.contains((metricFieldKey = metricKeys.get(0)), SdcctStringUtils.PERIOD_CHAR)) {
                    jsonGen.writeObjectField(SdcctMarkerUtils.buildFieldName(metricKeyPrefix),
                        (((metricFieldValue = metricMap.get(metricFieldKey)) instanceof Gauge<?>)
                            ? ((Gauge<?>) metricFieldValue).getValue() : metricFieldValue));
                } else {
                    serializeMetricField(jsonGen, metricKeyPrefix,
                        metricKeys.stream().sorted(String.CASE_INSENSITIVE_ORDER)
                            .collect(Collectors.toMap(
                                (String metricKey) -> StringUtils.removeStartIgnoreCase(metricKey, (metricKeyPrefix + SdcctStringUtils.PERIOD_CHAR)),
                                ((Function<String, Object>) metricMap::get))));
                }
            }
        }

        if (metricFieldNameAvailable) {
            jsonGen.writeEndObject();
        }
    }
}
