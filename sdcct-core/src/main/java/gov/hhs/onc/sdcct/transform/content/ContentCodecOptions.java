package gov.hhs.onc.sdcct.transform.content;

import gov.hhs.onc.sdcct.config.Option;
import gov.hhs.onc.sdcct.config.Options;
import gov.hhs.onc.sdcct.config.impl.OptionImpl;

public interface ContentCodecOptions<T extends ContentCodecOptions<T>> extends Options<T> {
    public final static Option<Boolean> PRETTY = new OptionImpl<>("pretty", Boolean.class);
}
