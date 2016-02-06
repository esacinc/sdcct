package gov.hhs.onc.sdcct.transform.content;

import gov.hhs.onc.sdcct.config.Options;
import java.util.Properties;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ParseOptions;

public interface ContentCodecOptions<T extends ContentCodecOptions<T>> extends Options<T> {
    public boolean hasParseOptions();

    @Nullable
    public ParseOptions getParseOptions();

    public T setParseOptions(@Nullable ParseOptions parseOpts);

    public boolean hasOutputProperties();

    @Nullable
    public Properties getOutputProperties();

    public T setOutputProperties(@Nullable Properties outProps);
}
