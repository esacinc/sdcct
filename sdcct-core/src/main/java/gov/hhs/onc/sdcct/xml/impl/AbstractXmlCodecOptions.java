package gov.hhs.onc.sdcct.xml.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodecOptions;
import gov.hhs.onc.sdcct.xml.XmlCodecOptions;
import java.util.Properties;
import javax.annotation.Nullable;
import net.sf.saxon.lib.ParseOptions;

public abstract class AbstractXmlCodecOptions<T extends XmlCodecOptions<T>> extends AbstractContentCodecOptions<T> implements XmlCodecOptions<T> {
    protected ParseOptions parseOpts;
    protected Properties outProps = new Properties();

    private final static long serialVersionUID = 0L;

    protected AbstractXmlCodecOptions(ParseOptions parseOpts) {
        super();

        this.parseOpts = new ParseOptions(parseOpts);
    }

    @Override
    protected void mergeInternal(T mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.parseOpts.merge(mergeOpts.getParseOptions());

        this.outProps.putAll(mergeOpts.getOutputProperties());
    }

    @Override
    public ParseOptions getParseOptions() {
        return this.parseOpts;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T addOutputProperty(String outPropName, @Nullable Object outPropValue) {
        // noinspection ConstantConditions
        this.outProps.put(outPropName, outPropValue);

        return ((T) this);
    }

    @Override
    public boolean hasOutputProperties() {
        return !this.outProps.isEmpty();
    }

    @Override
    public Properties getOutputProperties() {
        return this.outProps;
    }
}
