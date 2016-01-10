package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongUnaryOperator;

public class AtomicEventId {
    private static class AtomicEventCounter extends AtomicLong {
        private final static LongUnaryOperator NEXT_UPDATE_OP = (value) -> ((value < Long.MAX_VALUE) ? ++value : 1);

        private final static long serialVersionUID = 0L;

        public long getNext() {
            return this.updateAndGet(NEXT_UPDATE_OP);
        }
    }

    private AtomicEventCounter counter = new AtomicEventCounter();

    public String getNext() {
        return (System.currentTimeMillis() + SdcctStringUtils.HYPHEN + this.counter.getNext());
    }
}
