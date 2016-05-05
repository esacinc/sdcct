package gov.hhs.onc.sdcct.transform.content.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.config.impl.AbstractOptions;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import java.util.Properties;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ParseOptions;

public abstract class AbstractContentCodecOptions<T extends ContentCodecOptions<T>> extends AbstractOptions<T> implements ContentCodecOptions<T> {
    protected ParseOptions parseOpts;
    protected Properties outProps;

    private final static long serialVersionUID = 0L;

    protected AbstractContentCodecOptions(Supplier<T> optsBuilder) {
        this(optsBuilder, null, null);
    }

    protected AbstractContentCodecOptions(Supplier<T> optsBuilder, @Nullable Properties outProps) {
        this(optsBuilder, null, outProps);
    }

    protected AbstractContentCodecOptions(Supplier<T> optsBuilder, @Nullable ParseOptions parseOpts) {
        this(optsBuilder, parseOpts, null);
    }

    protected AbstractContentCodecOptions(Supplier<T> optsBuilder, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) {
        super(optsBuilder);

        this.parseOpts = parseOpts;
        this.outProps = outProps;
    }

    @Override
    protected void mergeInternal(T mergeOpts) {
        super.mergeInternal(mergeOpts);

        if (mergeOpts.hasParseOptions()) {
            ParseOptions mergeParseOpts = mergeOpts.getParseOptions();

            if (this.hasParseOptions()) {
                // noinspection ConstantConditions
                this.parseOpts.merge(mergeParseOpts);
            } else {
                // noinspection ConstantConditions
                this.parseOpts = new ParseOptions(mergeParseOpts);
            }
        }

        if (mergeOpts.hasOutputProperties()) {
            // noinspection ConstantConditions
            (this.hasOutputProperties() ? this.outProps : (this.outProps = new Properties())).putAll(mergeOpts.getOutputProperties());
        }
    }

    @Override
    public boolean hasParseOptions() {
        return (this.parseOpts != null);
    }

    @Nullable
    @Override
    public ParseOptions getParseOptions() {
        return this.parseOpts;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setParseOptions(@Nullable ParseOptions parseOpts) {
        this.parseOpts = parseOpts;

        return ((T) this);
    }

    @Override
    public boolean hasOutputProperties() {
        return (this.outProps != null);
    }

    @Nullable
    @Override
    public Properties getOutputProperties() {
        return this.outProps;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setOutputProperties(@Nullable Properties outProps) {
        this.outProps = outProps;

        return ((T) this);
    }
}
