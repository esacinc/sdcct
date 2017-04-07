package gov.hhs.onc.sdcct.testcases.utils;

import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import org.apache.cxf.message.Message;

public final class SdcctTestcaseUtils {
    private SdcctTestcaseUtils() {
    }

    public static <T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
        W addWsRequestEvent(Message msg, String resultPropName, Class<W> resultClass) {
        W result = SdcctWsPropertyUtils.getProperty(SdcctWsPropertyUtils.getContextualProperties(msg), resultPropName, resultClass);
        WsRequestEvent wsRequestEvent = SdcctWsPropertyUtils.getProperty(msg, WsRequestEvent.class);

        if ((result != null) && (wsRequestEvent != null)) {
            result.setWsRequestEvent(wsRequestEvent);
        }

        return result;
    }

    public static <T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
        W addWsResponseEvent(Message msg, String resultPropName, Class<W> resultClass, Message responseMsg) {
        W result = SdcctWsPropertyUtils.getProperty(SdcctWsPropertyUtils.getContextualProperties(msg), resultPropName, resultClass);
        WsResponseEvent wsResponseEvent = SdcctWsPropertyUtils.getProperty(responseMsg, WsResponseEvent.class);

        if ((result != null) && (wsResponseEvent != null)) {
            result.setWsResponseEvent(wsResponseEvent);
        }

        return result;
    }
}
