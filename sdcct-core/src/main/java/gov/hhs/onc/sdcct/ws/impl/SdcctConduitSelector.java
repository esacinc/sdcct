package gov.hhs.onc.sdcct.ws.impl;

import gov.hhs.onc.sdcct.logging.impl.TxTaskExecutor;
import org.apache.cxf.endpoint.DeferredConduitSelector;

public class SdcctConduitSelector extends DeferredConduitSelector {
    private TxTaskExecutor taskExec;

    public SdcctConduitSelector(TxTaskExecutor taskExec) {
        super();

        this.taskExec = taskExec;
    }

    public TxTaskExecutor getTaskExecutor() {
        return this.taskExec;
    }
}
