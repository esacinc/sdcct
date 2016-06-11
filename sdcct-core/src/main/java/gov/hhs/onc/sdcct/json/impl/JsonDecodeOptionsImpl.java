package gov.hhs.onc.sdcct.json.impl;

import gov.hhs.onc.sdcct.json.JsonDecodeOptions;

public class JsonDecodeOptionsImpl extends AbstractJsonCodecOptions<JsonDecodeOptions> implements JsonDecodeOptions {
    private final static long serialVersionUID = 0L;

    public JsonDecodeOptionsImpl() {
        super();
    }

    @Override
    protected JsonDecodeOptions cloneInternal() {
        return new JsonDecodeOptionsImpl();
    }
}
