package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.data.impl.TxId;
import java.util.Map;
import org.slf4j.MDC;

public class TxTaskWrapper implements Runnable {
    private Map<String, TxId> txIds;
    private Runnable task;

    public TxTaskWrapper(Map<String, TxId> txIds, Runnable task) {
        this.txIds = txIds;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            this.txIds.forEach((txMdcPropName, txId) -> MDC.put(txMdcPropName, txId.getNext()));

            this.task.run();
        } finally {
            this.txIds.keySet().forEach(MDC::remove);
        }
    }
}
