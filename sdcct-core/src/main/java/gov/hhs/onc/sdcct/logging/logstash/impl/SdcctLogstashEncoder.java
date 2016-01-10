package gov.hhs.onc.sdcct.logging.logstash.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.stream.Stream;
import net.logstash.logback.composite.JsonProvider;
import net.logstash.logback.composite.JsonProviders;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;

public class SdcctLogstashEncoder extends LoggingEventCompositeJsonEncoder {
    public void setObjectMapper(ObjectMapper objMapper) {
        this.getFormatter().getJsonFactory().setCodec(objMapper);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setProviderItems(JsonProvider<ILoggingEvent> ... provs) {
        JsonProviders<ILoggingEvent> provsContainer = this.getProviders();

        Stream.of(provs).forEach(provsContainer::addProvider);
    }
}
