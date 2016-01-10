package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.sdcct.transform.render.RenderOptions;
import gov.hhs.onc.sdcct.transform.render.RenderType;
import gov.hhs.onc.sdcct.transform.render.impl.AbstractSdcctRenderer;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;

public class JsonRenderer extends AbstractSdcctRenderer {
    @Resource(name = "objMapper")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper objMapper;

    @Resource(name = "objMapperPretty")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper formattingObjMapper;

    public JsonRenderer() {
        super(RenderType.JSON);
    }

    @Override
    protected byte[] renderInternal(Object obj, Map<String, Object> opts) throws Exception {
        return ((opts.containsKey(RenderOptions.PRETTY_NAME)
            ? BooleanUtils.toBooleanObject(opts.get(RenderOptions.PRETTY_NAME).toString().toLowerCase())
            : ((Boolean) this.defaultOpts.get(RenderOptions.PRETTY_NAME))) ? this.formattingObjMapper : this.objMapper).writeValueAsBytes(obj);
    }
}
