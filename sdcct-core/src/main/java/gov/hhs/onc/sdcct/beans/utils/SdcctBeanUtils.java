package gov.hhs.onc.sdcct.beans.utils;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.springframework.beans.BeanUtils;

public final class SdcctBeanUtils {
    private SdcctBeanUtils() {
    }

    @Nullable
    public static PropertyDescriptor findProperty(Class<?> clazz, Class<?> propClass, @Nullable Boolean readable, @Nullable Boolean writeable,
        Stream<String> propNames) {
        return streamProperties(clazz, propClass, readable, writeable, propNames).findFirst().orElse(null);
    }

    public static List<PropertyDescriptor> findProperties(Class<?> clazz, Class<?> propClass, @Nullable Boolean readable, @Nullable Boolean writeable,
        Stream<String> propNames) {
        return streamProperties(clazz, propClass, readable, writeable, propNames).collect(Collectors.toList());
    }

    public static Stream<PropertyDescriptor> streamProperties(Class<?> clazz, Class<?> propClass, @Nullable Boolean readable, @Nullable Boolean writeable,
        Stream<String> propNames) {
        boolean readableAvailable = (readable != null), writeableAvailable = (writeable != null);

        return propNames.map(propName -> BeanUtils.getPropertyDescriptor(clazz, propName))
            .filter(propDesc -> ((propDesc != null) && propClass.isAssignableFrom(propDesc.getPropertyType()) &&
                (!readableAvailable || (propDesc.getReadMethod() != null)) && (!writeableAvailable || (propDesc.getWriteMethod() != null))));
    }
}
