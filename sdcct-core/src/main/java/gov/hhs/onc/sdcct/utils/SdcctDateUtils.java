package gov.hhs.onc.sdcct.utils;

public final class SdcctDateUtils {
    public final static long HOURS_IN_DAY = 24L;

    public final static long MIN_IN_HOUR = 60L;
    public final static long MIN_IN_DAY = MIN_IN_HOUR * HOURS_IN_DAY;

    public final static long SEC_IN_MIN = 60L;
    public final static long SEC_IN_HOUR = SEC_IN_MIN * MIN_IN_HOUR;
    public final static long SEC_IN_DAY = SEC_IN_MIN * MIN_IN_DAY;
    public final static long SEC_IN_YEAR = 31556952L;

    public final static long MS_IN_SEC = 1000L;
    public final static long MS_IN_MIN = MS_IN_SEC * SEC_IN_MIN;
    public final static long MS_IN_HOUR = MS_IN_SEC * SEC_IN_HOUR;
    public final static long MS_IN_DAY = MS_IN_SEC * SEC_IN_DAY;
    public final static long MS_IN_YEAR = MS_IN_SEC * SEC_IN_YEAR;

    private SdcctDateUtils() {
    }
}
