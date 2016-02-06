package gov.hhs.onc.sdcct.transform.content.impl;

import java.util.Properties;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ParseOptions;

public class ContentDecodeOptions extends AbstractContentCodecOptions<ContentDecodeOptions> {
    private final static long serialVersionUID = 0L;

    public ContentDecodeOptions() {
        this(null, null);
    }

    public ContentDecodeOptions(@Nullable Properties outProps) {
        this(null, outProps);
    }

    public ContentDecodeOptions(@Nullable ParseOptions parseOpts) {
        this(parseOpts, null);
    }

    public ContentDecodeOptions(@Nullable ParseOptions parseOpts, @Nullable Properties outProps) {
        super(ContentDecodeOptions::new, parseOpts, outProps);
    }
}
