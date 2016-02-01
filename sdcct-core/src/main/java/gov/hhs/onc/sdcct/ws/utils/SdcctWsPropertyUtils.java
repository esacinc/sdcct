package gov.hhs.onc.sdcct.ws.utils;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ClassUtils;
import org.apache.cxf.message.Message;

public final class SdcctWsPropertyUtils {
    private SdcctWsPropertyUtils() {
    }

    public static boolean containsContent(Message msg, Class<?> contentClass) {
        return msg.getContentFormats().contains(contentClass);
    }

    public static boolean containsKey(Map<String, Object> props, Class<?> propValueClass) {
        return props.containsKey(propValueClass.getName());
    }

    @Nullable
    public static <T> T getProperty(Map<String, Object> props, Class<T> propValueClass) {
        return getProperty(props, propValueClass, null);
    }

    @Nullable
    public static <T> T getProperty(Map<String, Object> props, Class<T> propValueClass, @Nullable T defaultPropValue) {
        return getProperty(props, propValueClass.getName(), propValueClass, defaultPropValue);
    }

    @Nullable
    public static <T> T getProperty(Map<String, Object> props, String propName, Class<T> propValueClass) {
        return getProperty(props, propName, propValueClass, null);
    }

    @Nullable
    public static <T> T getProperty(Map<String, Object> props, String propName, Class<T> propValueClass, @Nullable T defaultPropValue) {
        if (!props.containsKey(propName)) {
            return null;
        }

        Object propValue = props.get(propName);

        if (propValue == null) {
            return defaultPropValue;
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
        return getProperty(props, propName, ((String) null));
    }

    @Nullable
    public static String getProperty(Map<String, Object> props, String propName, @Nullable String defaultPropValue) {
        return Objects.toString(props.get(propName), defaultPropValue);
    }
}
