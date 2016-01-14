package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("patternConvThrowableProxyRootCause")
public class RootCauseThrowableProxyConverter extends ExtendedThrowableProxyConverter {
    public final static String WORD = "exRoot";

    public String throwableProxyToString(IThrowableProxy throwableProxy) {
        return (CoreConstants.CAUSED_BY + SdcctExceptionUtils.buildRootCauseStackTrace(((ThrowableProxy) throwableProxy).getThrowable()) + StringUtils.LF);
    }
}
