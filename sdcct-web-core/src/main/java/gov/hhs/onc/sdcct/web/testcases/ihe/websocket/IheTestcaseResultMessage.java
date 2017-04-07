package gov.hhs.onc.sdcct.web.testcases.ihe.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.web.testcases.ihe.websocket.impl.IheTestcaseResultMessageImpl;
import gov.hhs.onc.sdcct.web.testcases.websocket.SdcctTestcaseResultMessage;

@JsonSubTypes({ @Type(IheTestcaseResultMessageImpl.class) })
public interface IheTestcaseResultMessage extends SdcctTestcaseResultMessage<IheTestcaseResult<?, ?>> {
}
