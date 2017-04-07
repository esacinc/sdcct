package gov.hhs.onc.sdcct.testcases.results.impl;

import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResultHandler;
import org.springframework.context.ApplicationEventPublisher;

public abstract class AbstractSdcctTestcaseResultHandler<T extends SdcctTestcaseResult<?, ?, ?>> implements SdcctTestcaseResultHandler<T> {
    protected ApplicationEventPublisher appEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher appEventPublisher) {
        this.appEventPublisher = appEventPublisher;
    }
}
