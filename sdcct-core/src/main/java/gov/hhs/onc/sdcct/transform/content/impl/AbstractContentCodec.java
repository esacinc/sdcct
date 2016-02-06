package gov.hhs.onc.sdcct.transform.content.impl;

import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;

public abstract class AbstractContentCodec implements ContentCodec {
    protected ContentDecodeOptions defaultDecodeOpts = new ContentDecodeOptions();
    protected ContentEncodeOptions defaultEncodeOpts = new ContentEncodeOptions();
    protected SdcctContentType type;

    protected AbstractContentCodec(SdcctContentType type) {
        this.type = type;
    }

    @Override
    public ContentDecodeOptions getDefaultDecodeOptions() {
        return this.defaultDecodeOpts;
    }

    @Override
    public void setDefaultDecodeOptions(ContentDecodeOptions defaultDecodeOpts) {
        this.defaultDecodeOpts = defaultDecodeOpts.clone();
    }

    @Override
    public ContentEncodeOptions getDefaultEncodeOptions() {
        return this.defaultEncodeOpts;
    }

    @Override
    public void setDefaultEncodeOptions(ContentEncodeOptions defaultEncodeOpts) {
        this.defaultEncodeOpts = defaultEncodeOpts.clone();
    }

    @Override
    public SdcctContentType getType() {
        return this.type;
    }
}
