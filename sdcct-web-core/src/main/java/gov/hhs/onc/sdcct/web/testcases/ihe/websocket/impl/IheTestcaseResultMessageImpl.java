package gov.hhs.onc.sdcct.web.testcases.ihe.websocket.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.web.testcases.ihe.websocket.IheTestcaseResultMessage;
import gov.hhs.onc.sdcct.web.testcases.websocket.impl.AbstractSdcctTestcaseResultMessage;

@JsonTypeName("msgTestcaseResultIhe")
public class IheTestcaseResultMessageImpl extends AbstractSdcctTestcaseResultMessage<IheTestcaseResult<?, ?>> implements IheTestcaseResultMessage {
    public IheTestcaseResultMessageImpl(IheTestcaseResult<?, ?> payload) {
        super(payload);
    }
}
