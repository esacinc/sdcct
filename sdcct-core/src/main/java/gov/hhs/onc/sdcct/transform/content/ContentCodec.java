package gov.hhs.onc.sdcct.transform.content;

import gov.hhs.onc.sdcct.transform.content.impl.ContentDecodeOptions;
import gov.hhs.onc.sdcct.transform.content.impl.ContentEncodeOptions;
import javax.annotation.Nullable;

public interface ContentCodec {
    public byte[] encode(Object src, @Nullable ContentEncodeOptions opts) throws Exception;

    public <T> T decode(byte[] src, Class<T> resultClass, @Nullable ContentDecodeOptions opts) throws Exception;

    public ContentDecodeOptions getDefaultDecodeOptions();

    public void setDefaultDecodeOptions(ContentDecodeOptions defaultDecodeOpts);

    public ContentEncodeOptions getDefaultEncodeOptions();

    public void setDefaultEncodeOptions(ContentEncodeOptions defaultEncodeOpts);

    public SdcctContentType getType();
}
