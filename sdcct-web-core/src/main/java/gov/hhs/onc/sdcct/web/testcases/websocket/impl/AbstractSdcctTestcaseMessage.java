package gov.hhs.onc.sdcct.web.testcases.websocket.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import gov.hhs.onc.sdcct.web.testcases.websocket.SdcctTestcaseMessage;

public abstract class AbstractSdcctTestcaseMessage<T> implements SdcctTestcaseMessage<T> {
    protected T payload;

    @JsonCreator
    protected AbstractSdcctTestcaseMessage(T payload) {
        this.payload = payload;
    }

    @Override
    public T getPayload() {
        return this.payload;
    }
}
