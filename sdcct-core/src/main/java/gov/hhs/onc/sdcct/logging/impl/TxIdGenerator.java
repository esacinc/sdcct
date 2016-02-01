package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.utils.SdcctDateUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.IdGenerator;

/**
 * Derived from:
 * <ul>
 * <li><a href="http://tools.ietf.org/html/rfc4122">RFC 4122 - A Universally Unique IDentifier (UUID) URN Namespace</a></li>
 * </ul>
 * 
 * A summary is available here: <a href="https://en.wikipedia.org/wiki/Universally_unique_identifier">Universally unique identifier</a>
 * 
 * Generated UUIDs are variant 2 (Leach-Salz), version 1 (time-based) customized to use the last 6 bytes of the SHA-1 digest of a given string as the node ID
 * (instead of a MAC address).
 */
public class TxIdGenerator implements IdGenerator, InitializingBean, NamedBean {
    private final static long NUM_INTERVALS_PER_MS = (SdcctDateUtils.NS_IN_MS / 100L);

    private final static long TIMESTAMP_GREGORIAN_CALENDAR_OFFSET = 12219292800000L;
    private final static int TIMESTAMP_DATA_SIZE = 8;
    private final static int TIMESTAMP_LOW_SRC_DATA_START_INDEX = 4;
    private final static int TIMESTAMP_LOW_DEST_DATA_START_INDEX = 0;
    private final static int TIMESTAMP_LOW_DATA_SIZE = 4;
    private final static int TIMESTAMP_MID_SRC_DATA_START_INDEX = 2;
    private final static int TIMESTAMP_MID_DEST_DATA_START_INDEX = 4;
    private final static int TIMESTAMP_MID_DATA_SIZE = 2;
    private final static int TIMESTAMP_HI_SRC_DATA_START_INDEX = 0;
    private final static int TIMESTAMP_HI_DEST_DATA_START_INDEX = 6;
    private final static int TIMESTAMP_HI_DATA_SIZE = 2;

    private final static int VERSION_DATA_INDEX = 6;
    private final static int VERSION_CLEAR_DATA_MASK = 0x0f;
    private final static int VERSION_1_DATA_MASK = 0x1;

    private final static int VARIANT_CLEAR_DATA_MASK = 0x3f;
    private final static int VARIANT_2_DATA_MASK = 0x8;

    private final static short CLOCK_SEQ_MAX = 0x3fff;
    private final static int CLOCK_SEQ_HI_DATA_INDEX = 8;
    private final static int CLOCK_SEQ_LOW_DATA_INDEX = 9;

    private final static int NODE_ID_DATA_START_INDEX = 10;
    private final static int NODE_ID_DATA_SIZE = 6;
    private final static int NODE_ID_TYPE_DATA_INDEX = 0;
    private final static int NODE_ID_TYPE_MULTICAST_DATA_MASK = 0x8;

    private final static int DATA_SIZE = 16;

    private MessageDigest msgDigest;
    private String name;
    private byte[] nodeId;
    private short clockSeq = -1;
    private long lastTimestamp = -1;
    private long numGenInMs = 0;

    public TxIdGenerator(MessageDigest msgDigest, String name) {
        this.msgDigest = msgDigest;
        this.name = name;
    }

    @Override
    public synchronized UUID generateId() {
        return this.generateId(System.currentTimeMillis());
    }

    public synchronized UUID generateId(@Nonnegative long timestamp) {
        if (timestamp != this.lastTimestamp) {
            this.numGenInMs = 0;
        } else if (this.numGenInMs == NUM_INTERVALS_PER_MS) {
            throw new FatalBeanException(String.format("Too many transaction IDs (name=%s) generated in current millisecond: %d > %d", this.name,
                (this.numGenInMs + 1), NUM_INTERVALS_PER_MS));
        }

        if (((this.lastTimestamp == -1) || (timestamp <= this.lastTimestamp)) && (++this.clockSeq > CLOCK_SEQ_MAX)) {
            this.clockSeq = 0;
        }

        byte[] data = new byte[DATA_SIZE], timestampData = new byte[TIMESTAMP_DATA_SIZE];

        timestamp = (((this.lastTimestamp = timestamp) + TIMESTAMP_GREGORIAN_CALENDAR_OFFSET) * NUM_INTERVALS_PER_MS) + this.numGenInMs++;

        for (int a = (TIMESTAMP_DATA_SIZE - 1); a >= 0; a--) {
            timestampData[a] = ((byte) timestamp);

            timestamp >>>= 8;
        }

        System.arraycopy(timestampData, TIMESTAMP_LOW_SRC_DATA_START_INDEX, data, TIMESTAMP_LOW_DEST_DATA_START_INDEX, TIMESTAMP_LOW_DATA_SIZE);
        System.arraycopy(timestampData, TIMESTAMP_MID_SRC_DATA_START_INDEX, data, TIMESTAMP_MID_DEST_DATA_START_INDEX, TIMESTAMP_MID_DATA_SIZE);
        System.arraycopy(timestampData, TIMESTAMP_HI_SRC_DATA_START_INDEX, data, TIMESTAMP_HI_DEST_DATA_START_INDEX, TIMESTAMP_HI_DATA_SIZE);

        data[VERSION_DATA_INDEX] &= VERSION_CLEAR_DATA_MASK;
        data[VERSION_DATA_INDEX] |= VERSION_1_DATA_MASK;

        data[CLOCK_SEQ_HI_DATA_INDEX] = ((byte) ((this.clockSeq & VARIANT_CLEAR_DATA_MASK) >>> 8));
        data[CLOCK_SEQ_HI_DATA_INDEX] |= VARIANT_2_DATA_MASK;
        data[CLOCK_SEQ_LOW_DATA_INDEX] = ((byte) (this.clockSeq & 0xff));

        System.arraycopy(this.nodeId, 0, data, NODE_ID_DATA_START_INDEX, NODE_ID_DATA_SIZE);

        long dataMostSigBits = 0, dataLeastSigBits = 0;

        for (int a = 0; a < 8; a++) {
            dataMostSigBits = ((dataMostSigBits << 8) | (data[a] & 0xff));
        }

        for (int a = 8; a < DATA_SIZE; a++) {
            dataLeastSigBits = ((dataLeastSigBits << 8) | (data[a] & 0xff));
        }

        return new UUID(dataMostSigBits, dataLeastSigBits);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        (this.nodeId = ArrayUtils.subarray((this.nodeId = this.msgDigest.digest(name.getBytes(StandardCharsets.UTF_8))),
            (this.nodeId.length - NODE_ID_DATA_SIZE), this.nodeId.length))[NODE_ID_TYPE_DATA_INDEX] |= NODE_ID_TYPE_MULTICAST_DATA_MASK;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
