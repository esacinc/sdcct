package gov.hhs.onc.sdcct.api.type.utils;

import gov.hhs.onc.sdcct.SdcctException;
import gov.hhs.onc.sdcct.api.type.DatatypeKindType;
import gov.hhs.onc.sdcct.beans.StaticValueSetBean;
import gov.hhs.onc.sdcct.beans.TypeBean;
import gov.hhs.onc.sdcct.utils.SdcctClassUtils;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import java.lang.reflect.Field;
import javax.annotation.Nullable;
import org.apache.commons.lang3.reflect.FieldUtils;

public final class SdcctDatatypeUtils {
    public final static String TYPE_FIELD_NAME_PREFIX = "TYPE";

    public final static String TYPE_KIND_FIELD_NAME = TYPE_FIELD_NAME_PREFIX + "_KIND";
    public final static String TYPE_PATH_FIELD_NAME = TYPE_FIELD_NAME_PREFIX + "_PATH";

    private SdcctDatatypeUtils() {
    }

    @Nullable
    public static <T extends Enum<T> & StaticValueSetBean> T getType(Class<T> valueClass, Class<?> clazz) {
        String path = getPath(clazz);

        return ((path != null) ? SdcctEnumUtils.findById(valueClass, path) : null);
    }

    @Nullable
    public static String getPath(Class<?> clazz) {
        return getConstantValue(TYPE_PATH_FIELD_NAME, String.class, clazz);
    }

    @Nullable
    public static DatatypeKindType getKind(Class<?> clazz) {
        return getConstantValue(TYPE_KIND_FIELD_NAME, DatatypeKindType.class, clazz);
    }

    @Nullable
    public static Class<?> buildImplClass(Class<?> clazz) {
        if (!TypeBean.class.isAssignableFrom(clazz)) {
            return null;
        }

        ClassLoader classLoader = clazz.getClassLoader();

        if (!SdcctClassUtils.isImplClass(clazz)) {
            try {
                return SdcctClassUtils.buildImplClass(classLoader, Object.class, clazz);
            } catch (ClassNotFoundException ignored) {
                return null;
            }
        }

        return clazz;
    }

    @Nullable
    private static <T> T getConstantValue(String fieldName, Class<T> valueClass, Class<?> clazz) {
        Class<?> implClass = buildImplClass(clazz);

        if (implClass == null) {
            return null;
        }

        Field field = FieldUtils.getDeclaredField(clazz, fieldName);

        if (field == null) {
            return null;
        }

        try {
            return valueClass.cast(FieldUtils.readStaticField(field));
        } catch (IllegalAccessException e) {
            throw new SdcctException(String.format("Unable to read datatype (class=%s, implClass=%s) constant declared field (name=%s) value (class=%s).",
                clazz, implClass, fieldName, valueClass), e);
        }
    }
}
