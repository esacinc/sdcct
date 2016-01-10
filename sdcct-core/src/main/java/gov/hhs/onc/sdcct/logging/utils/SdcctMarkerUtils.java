package gov.hhs.onc.sdcct.logging.utils;

import gov.hhs.onc.sdcct.logging.MarkerFieldName;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.util.Iterator;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Marker;
import org.springframework.core.annotation.AnnotationUtils;

public final class SdcctMarkerUtils {
    private SdcctMarkerUtils() {
    }

    @Nullable
    public static <T extends Marker> T findByType(@Nullable Marker marker, Class<T> markerClass) {
        if (marker != null) {
            if (markerClass.isAssignableFrom(marker.getClass())) {
                return markerClass.cast(marker);
            } else if (marker.hasReferences()) {
                Iterator<Marker> refMarkerIterator = marker.iterator();

                while (refMarkerIterator.hasNext()) {
                    if ((marker = findByType(refMarkerIterator.next(), markerClass)) != null) {
                        return markerClass.cast(marker);
                    }
                }
            }
        }

        return null;
    }

    public static String buildFieldName(Object fieldValue) {
        Class<?> fieldValueClass = fieldValue.getClass();
        MarkerFieldName fieldNameAnno = AnnotationUtils.findAnnotation(fieldValueClass, MarkerFieldName.class);
        String fieldName = ((fieldNameAnno != null) ? fieldNameAnno.value() : null);

        return buildFieldName((!StringUtils.isEmpty(fieldName) ? fieldName : fieldValueClass.getSimpleName()));
    }

    public static String buildFieldName(String fieldName) {
        return StringUtils.join(SdcctStringUtils.splitCamelCase(fieldName, SdcctStringUtils.UNDERSCORE), SdcctStringUtils.UNDERSCORE).toLowerCase();
    }
}
