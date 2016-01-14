package gov.hhs.onc.sdcct.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public final class SdcctExceptionUtils {
    private SdcctExceptionUtils() {
    }

    public static String buildRootCauseStackTrace(Throwable exception) {
        return StringUtils.join(ExceptionUtils.getRootCauseStackTrace(exception), StringUtils.LF);
    }

    public static Throwable getRootCause(Throwable throwable) {
        return ObjectUtils.defaultIfNull(ExceptionUtils.getRootCause(throwable), throwable);
    }
}
