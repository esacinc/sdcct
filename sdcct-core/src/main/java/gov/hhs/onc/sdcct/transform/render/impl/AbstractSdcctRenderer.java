package gov.hhs.onc.sdcct.transform.render.impl;

import gov.hhs.onc.sdcct.transform.render.RenderType;
import gov.hhs.onc.sdcct.transform.render.SdcctRenderer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSdcctRenderer implements SdcctRenderer {
    protected Map<String, Object> defaultOpts = new HashMap<>();
    protected RenderType type;

    protected AbstractSdcctRenderer(RenderType type) {
        this.type = type;
    }

    @Override
    public byte[] render(Object obj) throws Exception {
        return this.render(obj, Collections.emptyMap());
    }

    @Override
    public byte[] render(Object obj, Map<String, Object> opts) throws Exception {
        opts = new HashMap<>(opts);

        this.defaultOpts.forEach(opts::putIfAbsent);

        return this.renderInternal(obj, opts);
    }
    
    protected abstract byte[] renderInternal(Object obj, Map<String, Object> opts) throws Exception;

    @Override
    public Map<String, Object> getDefaultOptions() {
        return this.defaultOpts;
    }

    @Override
    public void setDefaultOptions(Map<String, Object> defaultOpts) {
        this.defaultOpts.clear();
        this.defaultOpts.putAll(defaultOpts);
    }

    @Override
    public RenderType getType() {
        return this.type;
    }
}
