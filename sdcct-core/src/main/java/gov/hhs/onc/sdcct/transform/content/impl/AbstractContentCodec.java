package gov.hhs.onc.sdcct.transform.content.impl;

import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractContentCodec implements ContentCodec {
    protected Map<String, Object> defaultDecodeOpts = new HashMap<>();
    protected Map<String, Object> defaultEncodeOpts = new HashMap<>();
    protected SdcctContentType type;

    protected AbstractContentCodec(SdcctContentType type) {
        this.type = type;
    }

    @Override
    public <T> T decode(byte[] src, Class<T> resultClass) throws Exception {
        return this.decode(src, resultClass, Collections.emptyMap());
    }

    @Override
    public byte[] encode(Object src) throws Exception {
        return this.encode(src, Collections.emptyMap());
    }

    @Override
    public Map<String, Object> getDefaultDecodeOptions() {
        return this.defaultDecodeOpts;
    }

    @Override
    public void setDefaultDecodeOptions(Map<String, Object> defaultDecodeOpts) {
        this.defaultDecodeOpts.clear();
        this.defaultDecodeOpts.putAll(defaultDecodeOpts);
    }

    @Override
    public Map<String, Object> getDefaultEncodeOptions() {
        return this.defaultEncodeOpts;
    }

    @Override
    public void setDefaultEncodeOptions(Map<String, Object> defaultEncodeOpts) {
        this.defaultEncodeOpts.clear();
        this.defaultEncodeOpts.putAll(defaultEncodeOpts);
    }

    @Override
    public SdcctContentType getType() {
        return this.type;
    }
}
