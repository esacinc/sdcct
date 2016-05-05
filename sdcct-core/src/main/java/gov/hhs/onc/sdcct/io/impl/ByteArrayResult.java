package gov.hhs.onc.sdcct.io.impl;

import java.io.ByteArrayOutputStream;
import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamResult;

public class ByteArrayResult extends StreamResult {
    public ByteArrayResult() {
        this(null);
    }

    public ByteArrayResult(@Nullable String sysId) {
        this(sysId, new ByteArrayOutputStream());
    }

    public ByteArrayResult(@Nullable String sysId, ByteArrayOutputStream outStream) {
        super(outStream);

        this.setSystemId(sysId);
    }

    public byte[] getBytes() {
        return this.getOutputStream().toByteArray();
    }

    @Override
    public ByteArrayOutputStream getOutputStream() {
        return ((ByteArrayOutputStream) super.getOutputStream());
    }
}
