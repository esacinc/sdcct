package gov.hhs.onc.sdcct.web.testcases.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SdcctTestcaseMessage<T> {
    @JsonProperty
    public T getPayload();
}
