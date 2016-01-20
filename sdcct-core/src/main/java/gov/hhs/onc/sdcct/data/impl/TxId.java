package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongUnaryOperator;

public class TxId {
    private static class TxIdCounter extends AtomicLong {
        private final static LongUnaryOperator NEXT_UPDATE_OP = (value) -> ((value < Long.MAX_VALUE) ? ++value : 1);

        private final static long serialVersionUID = 0L;

        public long getNext() {
            return this.updateAndGet(NEXT_UPDATE_OP);
        }
    }

    private TxIdCounter counter = new TxIdCounter();

    public String getNext() {
        return (System.currentTimeMillis() + SdcctStringUtils.HYPHEN + this.counter.getNext());
    }
}
