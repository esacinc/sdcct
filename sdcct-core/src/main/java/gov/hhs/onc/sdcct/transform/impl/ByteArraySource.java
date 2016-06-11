package gov.hhs.onc.sdcct.transform.impl;

import com.sun.tools.xjc.reader.xmlschema.parser.LSInputSAXWrapper;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;

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

    public LSInputSAXWrapper getLsInput() {
        LSInputSAXWrapper lsIn = new LSInputSAXWrapper(this.getInputSource());
        lsIn.setEncoding(StandardCharsets.UTF_8.name());

        return lsIn;
    }

    public InputSource getInputSource() {
        InputSource inSrc = new InputSource(this.getInputStream());
        inSrc.setEncoding(StandardCharsets.UTF_8.name());
        inSrc.setPublicId(this.getPublicId());
        inSrc.setSystemId(this.getSystemId());

        return inSrc;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
