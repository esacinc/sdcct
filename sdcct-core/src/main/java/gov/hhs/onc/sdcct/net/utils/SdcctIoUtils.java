package gov.hhs.onc.sdcct.net.utils;

import java.io.IOException;
import java.io.OutputStream;

public final class SdcctIoUtils {
    public static class NoOpOutputStream extends OutputStream {
        public final static NoOpOutputStream INSTANCE = new NoOpOutputStream();

        @Override
        public void write(int data) throws IOException {
        }
    }

    private SdcctIoUtils() {
    }
}
