package gov.hhs.onc.sdcct.transform.content;

import javax.annotation.Nullable;

public interface ContentCodec<T extends ContentCodecOptions<T>, U extends ContentCodecOptions<U>> {
    public byte[] encode(Object src, @Nullable U opts) throws Exception;

    public <V> V decode(byte[] src, Class<V> resultClass, @Nullable T opts) throws Exception;

    public T getDefaultDecodeOptions();

    public U getDefaultEncodeOptions();

    public SdcctContentType getType();
}
