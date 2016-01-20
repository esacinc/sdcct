package gov.hhs.onc.sdcct.utils;

public final class SdcctDataUtils {
    public final static int MB_IN_GB = 1024;

    public final static int KB_IN_MB = 1024;
    public final static int KB_IN_GB = KB_IN_MB * MB_IN_GB;

    public final static int BYTES_IN_KB = 1024;
    public final static int BYTES_IN_MB = KB_IN_MB * BYTES_IN_KB;
    public final static int BYTES_IN_GB = KB_IN_GB * BYTES_IN_KB;

    private SdcctDataUtils() {
    }
}
