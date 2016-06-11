package gov.hhs.onc.sdcct.json.impl;

import gov.hhs.onc.sdcct.json.JsonCodecOptions;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodecOptions;

public abstract class AbstractJsonCodecOptions<T extends JsonCodecOptions<T>> extends AbstractContentCodecOptions<T> implements JsonCodecOptions<T> {
    private final static long serialVersionUID = 0L;

    protected AbstractJsonCodecOptions() {
        super();
    }
}
