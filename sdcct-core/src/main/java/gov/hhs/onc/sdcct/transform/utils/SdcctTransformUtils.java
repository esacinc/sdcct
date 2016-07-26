package gov.hhs.onc.sdcct.transform.utils;

import gov.hhs.onc.sdcct.beans.utils.SdcctBeanUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.Serializer.Property;
import org.apache.commons.lang3.ObjectUtils;

public final class SdcctTransformUtils {
    public final static Set<String> COLUMN_NUM_PROP_NAMES = Collections.singleton("columnNumber");
    public final static Set<String> LINE_NUM_PROP_NAMES = Collections.singleton("lineNumber");
    public final static Set<String> PUBLIC_ID_PROP_NAMES = buildPropertyNames("public", "ID", "Id");
    public final static Set<String> SYS_ID_PROP_NAMES = buildPropertyNames("system", "Id", "ID");

    private SdcctTransformUtils() {
    }

    public static Properties buildOutputProperties(Properties outProps) {
        return outProps.entrySet().stream().collect(SdcctStreamUtils
            .toMap(outPropEntry -> Property.get(((String) outPropEntry.getKey())).getQName().getStructuredQName(), Entry::getValue, Properties::new));
    }

    public static int getColumnNumber(Object obj) {
        // noinspection ConstantConditions
        return getPropertyValue(obj, Integer.class, COLUMN_NUM_PROP_NAMES, -1);
    }

    public static void setColumnNumber(Object obj, int colNum) {
        setPropertyValue(obj, Integer.class, COLUMN_NUM_PROP_NAMES, colNum);
    }

    public static int getLineNumber(Object obj) {
        // noinspection ConstantConditions
        return getPropertyValue(obj, Integer.class, LINE_NUM_PROP_NAMES, -1);
    }

    public static void setLineNumber(Object obj, int lineNum) {
        setPropertyValue(obj, Integer.class, LINE_NUM_PROP_NAMES, lineNum);
    }

    @Nullable
    public static String getPublicId(Object obj) {
        return getPropertyValue(obj, String.class, PUBLIC_ID_PROP_NAMES, null);
    }

    public static void setPublicId(Object obj, @Nullable String publicId) {
        setPropertyValue(obj, String.class, PUBLIC_ID_PROP_NAMES, publicId);
    }

    @Nullable
    public static String getSystemId(Object obj) {
        return getPropertyValue(obj, String.class, SYS_ID_PROP_NAMES, null);
    }

    public static void setSystemId(Object obj, @Nullable String sysId) {
        setPropertyValue(obj, String.class, SYS_ID_PROP_NAMES, sysId);
    }

    @Nullable
    private static <T> T getPropertyValue(Object obj, Class<T> propClass, Set<String> propNames, @Nullable T defaultPropValue) {
        Class<?> clazz = obj.getClass();
        PropertyDescriptor propDesc = SdcctBeanUtils.findProperty(clazz, propClass, true, null, propNames.stream());

        if (propDesc == null) {
            return null;
        }

        Method propGetterMethod = propDesc.getReadMethod();

        try {
            return ObjectUtils.defaultIfNull(propClass.cast(propGetterMethod.invoke(obj)), defaultPropValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(String.format("Unable to invoke object (class=%s) property (class=%s, names=%s) getter method (name=%s).",
                clazz.getName(), propClass.getName(), propNames, propGetterMethod.getName()), e);
        }
    }

    private static <T> void setPropertyValue(Object obj, Class<T> propClass, Set<String> propNames, @Nullable T propValue) {
        Class<?> clazz = obj.getClass();
        PropertyDescriptor propDesc = SdcctBeanUtils.findProperty(clazz, propClass, null, true, propNames.stream());

        if (propDesc == null) {
            return;
        }

        Method propSetterMethod = propDesc.getWriteMethod();

        try {
            propSetterMethod.invoke(obj, propValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(String.format("Unable to invoke object (class=%s) property (class=%s, names=%s) setter method (name=%s).",
                clazz.getName(), propClass.getName(), propNames, propSetterMethod.getName()), e);
        }
    }

    private static Set<String> buildPropertyNames(String propNamePrefix, String ... propNameSuffixes) {
        return Stream.of(propNameSuffixes).map(propNameSuffix -> (propNamePrefix + propNameSuffix)).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
