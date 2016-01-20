package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.data.impl.TxId;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class TxTaskExecutor extends CustomizableThreadFactory implements Executor {
    private final static long serialVersionUID = 0L;

    private Map<String, TxId> txIds;

    public TxTaskExecutor(Map<String, TxId> txIds) {
        this.txIds = txIds;
    }

    @Override
    public void execute(Runnable task) {
        Executors.newSingleThreadExecutor(this).execute(new TxTaskWrapper(this.txIds, task));
    }
}
