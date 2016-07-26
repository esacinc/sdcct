package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.beans.PublicIdentifiedBean;
import gov.hhs.onc.sdcct.beans.SystemIdentifiedBean;
import java.io.ByteArrayOutputStream;
import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamResult;

public class ByteArrayResult extends StreamResult implements PublicIdentifiedBean, SystemIdentifiedBean {
    private String publicId;

    public ByteArrayResult() {
        this(null);
    }

    public ByteArrayResult(@Nullable String sysId) {
        this(null, sysId);
    }

    public ByteArrayResult(@Nullable String publicId, @Nullable String sysId) {
        this(publicId, sysId, new ByteArrayOutputStream());
    }

    public ByteArrayResult(@Nullable String publicId, @Nullable String sysId, ByteArrayOutputStream outStream) {
        super(outStream);

        this.publicId = publicId;

        this.setSystemId(sysId);
    }

    public byte[] getBytes() {
        return this.getOutputStream().toByteArray();
    }

    @Override
    public ByteArrayOutputStream getOutputStream() {
        return ((ByteArrayOutputStream) super.getOutputStream());
    }

    public boolean hasPublicId() {
        return (this.publicId != null);
    }

    @Nullable
    @Override
    public String getPublicId() {
        return this.publicId;
    }

    public void setPublicId(@Nullable String publicId) {
        this.publicId = publicId;
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
