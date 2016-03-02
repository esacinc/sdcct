package gov.hhs.onc.sdcct.config.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collector;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;

public final class SdcctPropertiesUtils {
    private SdcctPropertiesUtils() {
    }

    public static Properties singleton(String propName, Object propValue) {
        Properties props = new Properties();
        props.put(propName, propValue);

        return props;
    }

    public static Properties clone(@Nullable Properties props) {
        return (MapUtils.isEmpty(props) ? new Properties() : props.entrySet().stream().collect(toProperties()));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T extends Entry<?, ?>> Collector<T, ?, Properties> toProperties() {
        return SdcctStreamUtils.toMap(Properties::new);
    }
}
