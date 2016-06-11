package gov.hhs.onc.sdcct.utils;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.beans.OrdinalIdentifiedBean;
import gov.hhs.onc.sdcct.beans.UriBean;
import java.net.URI;
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
    public static <T extends Enum<T> & NamedBean> T findByName(Class<T> enumClass, String name) {
        return findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getName(), name));
    }

    @Nullable
    public static <T extends Enum<T> & OrdinalIdentifiedBean> T findByOrdinalId(Class<T> enumClass, int ordinalId) {
        return findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getOrdinalId(), ordinalId));
    }

    @Nullable
    public static <T extends Enum<T> & UriBean> T findByUri(Class<T> enumClass, URI uri) {
        return findByPredicate(enumClass, enumItem -> Objects.equals(enumItem.getUri(), uri));
    }

    @Nullable
    public static <T extends Enum<T>> T findByPredicate(Class<T> enumClass, Predicate<T> predicate) {
        return Stream.of(enumClass.getEnumConstants()).filter(predicate).findFirst().orElse(null);
    }
}
