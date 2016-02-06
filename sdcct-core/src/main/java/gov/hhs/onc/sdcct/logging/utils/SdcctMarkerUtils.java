package gov.hhs.onc.sdcct.logging.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import gov.hhs.onc.sdcct.logging.MarkerFieldName;
import gov.hhs.onc.sdcct.utils.SdcctIteratorUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Marker;
import org.springframework.core.annotation.AnnotationUtils;

public final class SdcctMarkerUtils {
    private SdcctMarkerUtils() {
    }

    public static <T> void serializeFields(JsonGenerator jsonGen, Map<String, T> fields) throws IOException {
        serializeFields(jsonGen, fields, Function.identity());
    }

    public static <T> void serializeFields(JsonGenerator jsonGen, Map<String, T> fields, Function<T, ?> fieldValueProcessor) throws IOException {
        if (!fields.isEmpty()) {
            Map<String, List<String>> fieldNamePrefixes = fields.keySet().stream().sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.groupingBy(fieldName -> StringUtils.split(fieldName, SdcctStringUtils.PERIOD, 2)[0]));
            List<String> subFieldNames;
            int numSubFields, subFieldNameStartIndex;
            String subFieldName;
            Map<String, T> subFields;

            for (String fieldNamePrefix : fieldNamePrefixes.keySet()) {
                if (((numSubFields = (subFieldNames = fieldNamePrefixes.get(fieldNamePrefix)).size()) == 1)
                    && !StringUtils.contains((subFieldName = subFieldNames.get(0)), SdcctStringUtils.PERIOD_CHAR)) {
                    jsonGen.writeObjectField(buildFieldName(fieldNamePrefix), fieldValueProcessor.apply(fields.get(subFieldName)));
                } else {
                    subFieldNames.sort(String.CASE_INSENSITIVE_ORDER);

                    subFieldNameStartIndex = (fieldNamePrefix.length() + 1);
                    subFields = new HashMap<>(numSubFields);

                    for (String subFieldNameItem : subFieldNames) {
                        subFields.put(
                            ((subFieldNameStartIndex < subFieldNameItem.length()) ? subFieldNameItem.substring(subFieldNameStartIndex) : subFieldNameItem),
                            fields.get(subFieldNameItem));
                    }

                    jsonGen.writeObjectFieldStart(fieldNamePrefix);

                    serializeFields(jsonGen, subFields, fieldValueProcessor);

                    jsonGen.writeEndObject();
                }
            }
        }
    }

    public static String buildFieldName(Object fieldValue) {
        Class<?> fieldValueClass = fieldValue.getClass();
        MarkerFieldName fieldNameAnno = AnnotationUtils.findAnnotation(fieldValueClass, MarkerFieldName.class);
        String fieldName = ((fieldNameAnno != null) ? fieldNameAnno.value() : null);

        return buildFieldName((!StringUtils.isEmpty(fieldName) ? fieldName : fieldValueClass.getSimpleName()));
    }

    public static String buildFieldName(String fieldName) {
        return StringUtils.join(SdcctStringUtils.splitCamelCase(fieldName, SdcctStringUtils.UNDERSCORE), SdcctStringUtils.UNDERSCORE_CHAR).toLowerCase();
    }

    public static Stream<Marker> stream(@Nullable Marker marker) {
        return SdcctStreamUtils.ofTree(marker, markerItem -> (markerItem.hasReferences() ? SdcctIteratorUtils.stream(markerItem.iterator()) : Stream.empty()));
    }
}
