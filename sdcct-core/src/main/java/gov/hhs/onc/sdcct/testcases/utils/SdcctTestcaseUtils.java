package gov.hhs.onc.sdcct.testcases.utils;

import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SdcctTestcaseUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctTestcaseUtils.class);

    private SdcctTestcaseUtils() {
    }

    public static <T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
        W addWsRequestEvent(Message msg, String resultPropName, Class<W> resultClass) {
        W result = SdcctWsPropertyUtils.getProperty(SdcctWsPropertyUtils.getContextualProperties(msg), resultPropName, resultClass);
        WsRequestEvent wsRequestEvent = SdcctWsPropertyUtils.getProperty(msg, WsRequestEvent.class);

        if (result != null && wsRequestEvent != null) {
            result.setWsRequestEvent(wsRequestEvent);

            LOGGER.info(String.format("Added wsRequestEvent (class=%s) to result (class=%s).", WsRequestEvent.class.getName(), result.getClass().getName()));
        }

        return result;
    }

    public static <T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
        W addWsResponseEvent(Message msg, String resultPropName, Class<W> resultClass, Message responseMsg) {
        W result = SdcctWsPropertyUtils.getProperty(SdcctWsPropertyUtils.getContextualProperties(msg), resultPropName, resultClass);
        WsResponseEvent wsResponseEvent = SdcctWsPropertyUtils.getProperty(responseMsg, WsResponseEvent.class);

        if (result != null && wsResponseEvent != null) {
            result.setWsResponseEvent(wsResponseEvent);

            LOGGER.info(String.format("Added wsResponseEvent (class=%s) to result (class=%s).", WsResponseEvent.class.getName(), result.getClass().getName()));
        }

        return result;
    }
}
