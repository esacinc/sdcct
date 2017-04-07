package gov.hhs.onc.sdcct.web.testcases.websocket;

import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;

public interface SdcctTestcaseResultMessage<T extends SdcctTestcaseResult<?, ?, ?>> extends SdcctTestcaseMessage<T> {
}
