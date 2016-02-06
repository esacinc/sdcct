package gov.hhs.onc.sdcct.transform.content.impl;

import gov.hhs.onc.sdcct.config.Option;
import gov.hhs.onc.sdcct.config.impl.OptionImpl;
import java.util.Properties;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ParseOptions;

public class ContentEncodeOptions extends AbstractContentCodecOptions<ContentEncodeOptions> {
    public final static Option<Boolean> PRETTY = new OptionImpl<>("pretty", Boolean.class);

    private final static long serialVersionUID = 0L;

    public ContentEncodeOptions() {
        this(null, null);
    }

    public ContentEncodeOptions(@Nullable Properties outProps) {
        this(null, outProps);
    }

    public ContentEncodeOptions(@Nullable ParseOptions parseOpts) {
        this(parseOpts, null);
    }

    public ContentEncodeOptions(@Nullable ParseOptions parseOpts, @Nullable Properties outProps) {
        super(ContentEncodeOptions::new, parseOpts, outProps);
    }
}
