package gov.hhs.onc.sdcct.testcases.results.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResultHandler;
import org.springframework.context.ApplicationEvent;

public abstract class AbstractSdcctTestcaseResultAddedEvent<T extends SdcctTestcaseResult<?, ?, ?>, U extends SdcctTestcaseResultHandler<T>>
    extends ApplicationEvent {
    protected T result;

    private final static long serialVersionUID = 0L;

    protected AbstractSdcctTestcaseResultAddedEvent(U src, T result) {
        super(src);

        this.result = result;
    }

    public T getResult() {
        return this.result;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public U getSource() {
        return ((U) super.getSource());
    }
}
