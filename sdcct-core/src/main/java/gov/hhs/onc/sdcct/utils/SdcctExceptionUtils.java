package gov.hhs.onc.sdcct.utils;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public final class SdcctExceptionUtils {
    private SdcctExceptionUtils() {
    }

    public static String buildRootCauseStackTrace(Throwable exception) {
        return StringUtils.join(ExceptionUtils.getRootCauseStackTrace(exception), StringUtils.LF);
    }

    public static Throwable getRootCause(Throwable exception) {
        return ObjectUtils.defaultIfNull(ExceptionUtils.getRootCause(exception), exception);
    }

    @Nullable
    public static <T extends Throwable> T findCause(Throwable exception, Class<T> causeClass) {
        return SdcctStreamUtils.asInstances(Stream.of(ExceptionUtils.getThrowables(exception)), causeClass).findFirst().orElse(null);
    }
}
