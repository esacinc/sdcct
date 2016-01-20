package gov.hhs.onc.sdcct.logging.logstash.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import gov.hhs.onc.sdcct.logging.MdcPropertyNames;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.logstash.logback.composite.AbstractFieldJsonProvider;
import net.logstash.logback.composite.JsonWritingUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("jsonProvMdc")
public class SdcctMdcJsonProvider extends AbstractFieldJsonProvider<ILoggingEvent> {
    private final static String FIELD_NAME = "mdc";

    public SdcctMdcJsonProvider() {
        this.setFieldName(FIELD_NAME);
    }

    @Override
    public void writeTo(JsonGenerator jsonGen, ILoggingEvent event) throws IOException {
        Map<String, String> mdcProps = event.getMDCPropertyMap();

        if (mdcProps.isEmpty()) {
            return;
        }

        JsonWritingUtils.writeMapStringFields(jsonGen, this.getFieldName(),
            mdcProps.entrySet().stream().filter(mdcPropEntry -> StringUtils.startsWith(mdcPropEntry.getKey(), MdcPropertyNames.PREFIX)).collect(SdcctStreamUtils
                .toMap(mdcPropEntry -> StringUtils.removeStart(mdcPropEntry.getKey(), MdcPropertyNames.PREFIX), Entry::getValue, TreeMap::new)));
    }
}
