package gov.hhs.onc.sdcct.transform.content.impl;

import gov.hhs.onc.sdcct.config.impl.AbstractOptions;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;

public abstract class AbstractContentCodecOptions<T extends ContentCodecOptions<T>> extends AbstractOptions<T> implements ContentCodecOptions<T> {
    private final static long serialVersionUID = 0L;

    protected AbstractContentCodecOptions() {
        super();
    }
}
