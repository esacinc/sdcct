package gov.hhs.onc.sdcct.logging.impl;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public class TxTaskExecutor extends CustomizableThreadFactory implements Executor {
    private final static long serialVersionUID = 0L;

    private Map<String, TxIdGenerator> txIdGens;

    public TxTaskExecutor(Map<String, TxIdGenerator> txIdGens) {
        this.txIdGens = txIdGens;
    }

    @Override
    public void execute(Runnable task) {
        Executors.newSingleThreadExecutor(this).execute(new TxTaskWrapper(this.txIdGens, task));
    }
}
