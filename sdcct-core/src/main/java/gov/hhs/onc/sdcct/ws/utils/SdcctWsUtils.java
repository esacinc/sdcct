package gov.hhs.onc.sdcct.ws.utils;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ClassUtils;

public final class SdcctWsUtils {
    private SdcctWsUtils() {
    }

    @Nullable
    public static <T> T getProperty(Map<String, Object> props, String propName, Class<T> propValueClass) {
        if (!props.containsKey(propName)) {
            return null;
        }

        Object propValue = props.get(propName);

        if (propValue == null) {
            return null;
        }

        Class<?> actualPropValueClass = propValue.getClass();

        if (!ClassUtils.isAssignable(actualPropValueClass, propValueClass)) {
            throw new ClassCastException(String.format("Property (name=%s) value is not assignable (actualClass=%s, class=%s).", propName,
                actualPropValueClass.getName(), propValueClass.getName()));
        }

        return propValueClass.cast(propValue);
    }

    @Nullable
    public static String getProperty(Map<String, Object> props, String propName) {
        return Objects.toString(props.get(propName), null);
    }
}
