package gov.hhs.onc.sdcct.transform.content;

import gov.hhs.onc.sdcct.config.SdcctOption;
import gov.hhs.onc.sdcct.config.SdcctOptions;
import gov.hhs.onc.sdcct.config.impl.SdcctOptionImpl;

public interface ContentCodecOptions<T extends ContentCodecOptions<T>> extends SdcctOptions<T> {
    public final static SdcctOption<Boolean> PRETTY = new SdcctOptionImpl<>("pretty", Boolean.class);
}
