package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class RootCauseThrowableProxyConverter extends ExtendedThrowableProxyConverter {
    public final static String WORD = "exRoot";

    public String throwableProxyToString(IThrowableProxy throwableProxy) {
        String[] rootCauseStackTraceArr = ExceptionUtils.getRootCauseStackTrace(((ThrowableProxy) throwableProxy).getThrowable());
        rootCauseStackTraceArr[0] = CoreConstants.CAUSED_BY + rootCauseStackTraceArr[0];
        rootCauseStackTraceArr[rootCauseStackTraceArr.length - 1] += CoreConstants.LINE_SEPARATOR;

        return StringUtils.join(rootCauseStackTraceArr, CoreConstants.LINE_SEPARATOR);
    }
}
