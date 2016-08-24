package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.sdcct.json.JsonDecodeOptions;
import gov.hhs.onc.sdcct.json.JsonEncodeOptions;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodec;
import javax.annotation.Nullable;
import javax.annotation.Resource;

public class JsonCodec extends AbstractContentCodec<JsonDecodeOptions, JsonEncodeOptions> {
    @Resource(name = "objMapper")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper objMapper;

    @Resource(name = "objMapperPretty")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper prettyObjMapper;

    public JsonCodec(JsonDecodeOptions defaultDecodeOpts, JsonEncodeOptions defaultEncodeOpts) {
        super(SdcctContentType.JSON, defaultDecodeOpts, defaultEncodeOpts);
    }

    @Override
    public byte[] encode(Object src, @Nullable JsonEncodeOptions opts) throws Exception {
        return this.buildEncodeObjectMapper(opts).writeValueAsBytes(src);
    }

    @Override
    public <T> T decode(byte[] src, Class<T> resultClass, @Nullable JsonDecodeOptions opts) throws Exception {
        return this.buildDecodeObjectMapper(opts).readValue(src, resultClass);
    }

    public JsonNode decode(byte[] src, @Nullable JsonDecodeOptions opts) throws Exception {
        return this.buildDecodeObjectMapper(opts).readTree(src);
    }

    private ObjectMapper buildEncodeObjectMapper(@Nullable JsonEncodeOptions opts) {
        // noinspection ConstantConditions
        ObjectMapper srcObjMapper =
            ((opts = this.defaultEncodeOpts.clone().merge(opts)).getOption(ContentCodecOptions.PRETTY) ? this.prettyObjMapper : this.objMapper);

        if (opts.hasModules()) {
            (srcObjMapper = srcObjMapper.copy()).registerModules(opts.getModules());
        }

        return srcObjMapper;
    }

    private ObjectMapper buildDecodeObjectMapper(@Nullable JsonDecodeOptions opts) {
        ObjectMapper srcObjMapper = this.objMapper;

        if ((opts = this.defaultDecodeOpts.clone().merge(opts)).hasModules()) {
            (srcObjMapper = srcObjMapper.copy()).registerModules(opts.getModules());
        }

        return srcObjMapper;
    }
}
