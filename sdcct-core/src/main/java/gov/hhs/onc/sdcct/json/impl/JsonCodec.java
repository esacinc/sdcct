package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.sdcct.transform.content.ContentEncodeOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodec;
import gov.hhs.onc.sdcct.utils.SdcctOptionUtils;
import java.util.Map;
import javax.annotation.Resource;

public class JsonCodec extends AbstractContentCodec {
    @Resource(name = "objMapper")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper objMapper;

    @Resource(name = "objMapperPretty")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper prettyObjMapper;

    public JsonCodec() {
        super(SdcctContentType.JSON);
    }

    @Override
    public <T> T decode(byte[] src, Class<T> resultClass, Map<String, Object> opts) throws Exception {
        return this.objMapper.readValue(src, resultClass);
    }

    @Override
    public byte[] encode(Object src, Map<String, Object> opts) throws Exception {
        return (SdcctOptionUtils.getBooleanValue(opts, ContentEncodeOptions.PRETTY_NAME, this.defaultEncodeOpts) ? this.prettyObjMapper : this.objMapper)
            .writeValueAsBytes(src);
    }
}
