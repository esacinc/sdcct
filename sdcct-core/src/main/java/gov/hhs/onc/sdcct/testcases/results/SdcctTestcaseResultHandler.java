package gov.hhs.onc.sdcct.testcases.results;

import org.springframework.context.ApplicationEventPublisherAware;

public interface SdcctTestcaseResultHandler<T extends SdcctTestcaseResult<?, ?, ?>> extends ApplicationEventPublisherAware {
    public void addResult(T result);
}
