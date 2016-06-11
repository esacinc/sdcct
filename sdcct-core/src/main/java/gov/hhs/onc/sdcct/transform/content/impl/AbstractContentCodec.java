package gov.hhs.onc.sdcct.transform.content.impl;

import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;

public abstract class AbstractContentCodec<T extends ContentCodecOptions<T>, U extends ContentCodecOptions<U>> implements ContentCodec<T, U> {
    protected SdcctContentType type;
    protected T defaultDecodeOpts;
    protected U defaultEncodeOpts;

    protected AbstractContentCodec(SdcctContentType type, T defaultDecodeOpts, U defaultEncodeOpts) {
        this.type = type;
        this.defaultDecodeOpts = defaultDecodeOpts;
        this.defaultEncodeOpts = defaultEncodeOpts;
    }

    @Override
    public T getDefaultDecodeOptions() {
        return this.defaultDecodeOpts;
    }

    @Override
    public U getDefaultEncodeOptions() {
        return this.defaultEncodeOpts;
    }

    @Override
    public SdcctContentType getType() {
        return this.type;
    }
}
