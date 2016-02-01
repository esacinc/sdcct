package gov.hhs.onc.sdcct.logging.impl;

import java.util.Map;
import org.slf4j.MDC;

public class TxTaskWrapper implements Runnable {
    private Map<String, TxIdGenerator> txIdGens;
    private Runnable task;

    public TxTaskWrapper(Map<String, TxIdGenerator> txIdGens, Runnable task) {
        this.txIdGens = txIdGens;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            long timestamp = System.currentTimeMillis();

            this.txIdGens.forEach((txIdMdcPropName, txIdGen) -> MDC.put(txIdMdcPropName, txIdGen.generateId(timestamp).toString()));

            this.task.run();
        } finally {
            this.txIdGens.keySet().forEach(MDC::remove);
        }
    }
}
