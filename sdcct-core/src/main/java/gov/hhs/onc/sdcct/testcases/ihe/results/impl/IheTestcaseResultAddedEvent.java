package gov.hhs.onc.sdcct.testcases.ihe.results.impl;

import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.results.impl.AbstractSdcctTestcaseResultAddedEvent;

public class IheTestcaseResultAddedEvent extends AbstractSdcctTestcaseResultAddedEvent<IheTestcaseResult<?, ?>, IheTestcaseResultHandler> {
    private final static long serialVersionUID = 0L;

    public IheTestcaseResultAddedEvent(IheTestcaseResultHandler src, IheTestcaseResult<?, ?> result) {
        super(src, result);
    }
}
