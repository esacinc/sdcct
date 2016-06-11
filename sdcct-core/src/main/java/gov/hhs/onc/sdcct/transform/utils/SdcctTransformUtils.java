package gov.hhs.onc.sdcct.transform.utils;

import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import net.sf.saxon.s9api.Serializer.Property;
import org.apache.commons.lang3.reflect.MethodUtils;

public final class SdcctTransformUtils {
    public final static Set<String> SRC_PUBLIC_ID_GETTER_METHOD_NAMES =
        Stream.of("getPublicID", "getPublicId").collect(Collectors.toCollection(LinkedHashSet::new));

    private SdcctTransformUtils() {
    }

    public static Properties buildOutputProperties(Properties outProps) {
        return outProps.entrySet().stream().collect(SdcctStreamUtils
            .toMap(outPropEntry -> Property.get(((String) outPropEntry.getKey())).getQName().getStructuredQName(), Entry::getValue, Properties::new));
    }

    @Nullable
    public static String getPublicId(Source src) {
        Class<?> srcClass = src.getClass();
        Method srcPublicIdGetterMethod = SRC_PUBLIC_ID_GETTER_METHOD_NAMES.stream()
            .map(srcPublicIdGetterMethodName -> MethodUtils.getAccessibleMethod(srcClass, srcPublicIdGetterMethodName))
            .filter(
                srcPublicIdGetterMethodItem -> ((srcPublicIdGetterMethodItem != null) && (srcPublicIdGetterMethodItem.getReturnType().equals(String.class))))
            .findFirst().orElse(null);

        if (srcPublicIdGetterMethod != null) {
            try {
                return ((String) srcPublicIdGetterMethod.invoke(src));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(
                    String.format("Unable to invoke source (class=%s, sysId=%s) public ID getter method.", srcClass.getName(), src.getSystemId()), e);
            }
        }

        return null;
    }
}
