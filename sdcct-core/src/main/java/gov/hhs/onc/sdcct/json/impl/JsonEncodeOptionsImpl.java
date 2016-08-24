package gov.hhs.onc.sdcct.json.impl;

import gov.hhs.onc.sdcct.json.JsonEncodeOptions;

public class JsonEncodeOptionsImpl extends AbstractJsonCodecOptions<JsonEncodeOptions> implements JsonEncodeOptions {
    private final static long serialVersionUID = 0L;

    public JsonEncodeOptionsImpl() {
        super();
    }

    @Override
    protected JsonEncodeOptions cloneInternal() {
        return new JsonEncodeOptionsImpl();
    }
}
