package gov.hhs.onc.sdcct.web.testcases.websocket.impl;

import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.web.testcases.websocket.SdcctTestcaseResultMessage;

public abstract class AbstractSdcctTestcaseResultMessage<T extends SdcctTestcaseResult<?, ?, ?>> extends AbstractSdcctTestcaseMessage<T>
    implements SdcctTestcaseResultMessage<T> {
    protected AbstractSdcctTestcaseResultMessage(T payload) {
        super(payload);
    }
}
