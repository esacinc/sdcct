package gov.hhs.onc.sdcct.transform.content.impl;

import gov.hhs.onc.sdcct.config.impl.AbstractSdcctOptions;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;

public abstract class AbstractContentCodecOptions<T extends ContentCodecOptions<T>> extends AbstractSdcctOptions<T> implements ContentCodecOptions<T> {
    private final static long serialVersionUID = 0L;

    protected AbstractContentCodecOptions() {
        super();
    }
}
