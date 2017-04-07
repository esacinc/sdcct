package gov.hhs.onc.sdcct.testcases.ihe.results.impl;

import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.results.impl.AbstractSdcctTestcaseResultHandler;
import org.springframework.stereotype.Component;

@Component("testcaseResultHandlerIhe")
public class IheTestcaseResultHandlerImpl extends AbstractSdcctTestcaseResultHandler<IheTestcaseResult<?, ?>> implements IheTestcaseResultHandler {
    @Override
    public void addResult(IheTestcaseResult<?, ?> result) {
        this.appEventPublisher.publishEvent(new IheTestcaseResultAddedEvent(this, result));
    }
}
