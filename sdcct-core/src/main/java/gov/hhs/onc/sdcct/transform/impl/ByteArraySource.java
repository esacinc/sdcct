package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.beans.PublicIdentifiedBean;
import gov.hhs.onc.sdcct.beans.SystemIdentifiedBean;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;

public class ByteArraySource extends StreamSource implements LSInput, PublicIdentifiedBean, SystemIdentifiedBean {
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

    public InputSource getInputSource() {
        InputSource inSrc = new InputSource(this.getInputStream());
        inSrc.setEncoding(StandardCharsets.UTF_8.name());
        inSrc.setPublicId(this.getPublicId());
        inSrc.setSystemId(this.getSystemId());

        return inSrc;
    }

    @Override
    public ByteArrayInputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    @Nullable
    @Override
    public String getBaseURI() {
        return null;
    }

    @Override
    public void setBaseURI(@Nullable String baseUri) {
        throw new UnsupportedOperationException();
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public ByteArrayInputStream getByteStream() {
        return this.getInputStream();
    }

    @Override
    public void setByteStream(@Nullable InputStream byteStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCertifiedText() {
        return false;
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Reader getCharacterStream() {
        return null;
    }

    @Override
    public void setCharacterStream(@Nullable Reader charStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getEncoding() {
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public void setEncoding(String enc) {
        throw new UnsupportedOperationException();
    }

    public boolean hasPublicId() {
        return (this.getPublicId() != null);
    }

    @Nullable
    @Override
    public String getPublicId() {
        return super.getPublicId();
    }

    @Override
    public void setPublicId(@Nullable String publicId) {
        super.setPublicId(publicId);
    }

    @Nullable
    @Override
    public String getStringData() {
        return null;
    }

    @Override
    public void setStringData(@Nullable String strData) {
        throw new UnsupportedOperationException();
    }

    public boolean hasSystemId() {
        return (this.getSystemId() != null);
    }

    @Nullable
    @Override
    public String getSystemId() {
        return super.getSystemId();
    }

    @Override
    public void setSystemId(@Nullable String sysId) {
        super.setSystemId(sysId);
    }
}
