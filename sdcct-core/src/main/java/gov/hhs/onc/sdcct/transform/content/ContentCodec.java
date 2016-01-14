package gov.hhs.onc.sdcct.transform.content;

import java.util.Map;

public interface ContentCodec {
    public <T> T decode(byte[] src, Class<T> resultClass) throws Exception;

    public <T> T decode(byte[] src, Class<T> resultClass, Map<String, Object> opts) throws Exception;

    public byte[] encode(Object src) throws Exception;

    public byte[] encode(Object src, Map<String, Object> opts) throws Exception;

    public Map<String, Object> getDefaultDecodeOptions();

    public void setDefaultDecodeOptions(Map<String, Object> defaultDecodeOpts);

    public Map<String, Object> getDefaultEncodeOptions();

    public void setDefaultEncodeOptions(Map<String, Object> defaultEncodeOpts);

    public SdcctContentType getType();
}
