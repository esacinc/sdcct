package gov.hhs.onc.sdcct.io.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamSource;

public class ByteArraySource extends StreamSource {
    protected byte[] bytes;

    public ByteArraySource(byte[] bytes) {
        this(bytes, null);
    }

    public ByteArraySource(byte[] bytes, @Nullable String sysId) {
        this(bytes, null, sysId);
    }

    public ByteArraySource(byte[] bytes, @Nullable String publicId, @Nullable String sysId) {
        this(publicId, sysId);

        this.bytes = bytes;
    }

    protected ByteArraySource(@Nullable String publicId, @Nullable String sysId) {
        super(sysId);

        this.setPublicId(publicId);
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }
}
