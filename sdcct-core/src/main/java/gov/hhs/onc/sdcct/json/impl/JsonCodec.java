package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodec;
import gov.hhs.onc.sdcct.transform.content.impl.ContentDecodeOptions;
import gov.hhs.onc.sdcct.transform.content.impl.ContentEncodeOptions;
import javax.annotation.Nullable;
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
    public byte[] encode(Object src, @Nullable ContentEncodeOptions opts) throws Exception {
        // noinspection ConstantConditions
        return (this.defaultEncodeOpts.clone().merge(opts).getOption(ContentEncodeOptions.PRETTY) ? this.prettyObjMapper : this.objMapper)
            .writeValueAsBytes(src);
    }

    @Override
    public <T> T decode(byte[] src, Class<T> resultClass, @Nullable ContentDecodeOptions opts) throws Exception {
        return this.objMapper.readValue(src, resultClass);
    }

    public JsonNode decode(byte[] src, @Nullable ContentDecodeOptions opts) throws Exception {
        return this.objMapper.readTree(src);
    }
}
