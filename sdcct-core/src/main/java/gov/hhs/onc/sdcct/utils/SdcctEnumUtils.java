package gov.hhs.onc.sdcct.utils;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public final class SdcctEnumUtils {
    private SdcctEnumUtils() {
    }

    @Nullable
    public static <T extends Enum<T> & IdentifiedBean> T findById(Class<T> enumClass, String id) {
        return findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getId(), id));
    }

    @Nullable
    public static <T extends Enum<T>> T findByPredicate(Class<T> enumClass, Predicate<? super T> propPredicate) {
        return Stream.of(enumClass.getEnumConstants()).filter(propPredicate).findFirst().orElse(null);
    }
}
