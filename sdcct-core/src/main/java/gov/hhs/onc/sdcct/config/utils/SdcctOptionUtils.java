package gov.hhs.onc.sdcct.config.utils;

import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.BooleanUtils;

public final class SdcctOptionUtils {
    private SdcctOptionUtils() {
    }

    public static boolean getBooleanValue(Map<?, ?> opts, String optName) {
        return getBooleanValue(optName, opts.get(optName));
    }

    public static boolean getBooleanValue(Map<?, ?> opts, String optName, boolean defaultOptValue) {
        return (opts.containsKey(optName) ? getBooleanValue(optName, opts.get(optName), defaultOptValue) : defaultOptValue);
    }

    public static boolean getBooleanValue(String optName, @Nullable Object optValue) {
        return getBooleanValue(optName, optValue, null);
    }

    public static boolean getBooleanValue(String optName, @Nullable Object optValue, @Nullable Boolean defaultOptValue) {
        if (optValue == null) {
            if (defaultOptValue != null) {
                return defaultOptValue;
            }
        } else if (optValue instanceof Boolean) {
            return ((Boolean) optValue);
        } else {
            Boolean boolOptValue = BooleanUtils.toBooleanObject(optValue.toString().toLowerCase());

            if (boolOptValue != null) {
                return boolOptValue;
            }
        }

        throw new IllegalArgumentException(String.format("Invalid boolean option (name=%s) value: %s", optName, optValue));
    }

    public static int getIntegerValue(Map<?, ?> opts, String optName) {
        return getIntegerValue(optName, opts.get(optName));
    }

    public static int getIntegerValue(Map<?, ?> opts, String optName, int defaultOptValue) {
        return (opts.containsKey(optName) ? getIntegerValue(optName, opts.get(optName), defaultOptValue) : defaultOptValue);
    }

    public static int getIntegerValue(String optName, @Nullable Object optValue) {
        return getIntegerValue(optName, optValue, null);
    }

    public static int getIntegerValue(String optName, @Nullable Object optValue, @Nullable Integer defaultOptValue) {
        if (optValue == null) {
            if (defaultOptValue != null) {
                return defaultOptValue;
            }
        } else if (optValue instanceof Number) {
            return ((Number) optValue).intValue();
        } else {
            try {
                return Integer.parseInt(optValue.toString());
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalArgumentException(String.format("Invalid integer option (name=%s) value: %s", optName, optValue));
    }
}
