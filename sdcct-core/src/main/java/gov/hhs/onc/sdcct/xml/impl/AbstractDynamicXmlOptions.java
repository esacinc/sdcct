package gov.hhs.onc.sdcct.xml.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.xml.DynamicXmlOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.XdmNode;
import org.apache.commons.collections4.MapUtils;

public abstract class AbstractDynamicXmlOptions<T extends DynamicXmlOptions<T>> extends AbstractXmlOptions<T> implements
    DynamicXmlOptions<T> {
    protected Map<Object, Object> contextData = new HashMap<>();
    protected XdmNode contextNode;

    private final static long serialVersionUID = 0L;

    protected AbstractDynamicXmlOptions(Supplier<T> optsBuilder) {
        super(optsBuilder);
    }

    @Override
    protected void mergeInternal(T mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.contextData.putAll(mergeOpts.getContextData());

        if (mergeOpts.hasContextNode()) {
            this.contextNode = mergeOpts.getContextNode();
        }
    }

    @Override
    public boolean hasContextData() {
        return !this.contextData.isEmpty();
    }

    @Override
    public Map<Object, Object> getContextData() {
        return this.contextData;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setContextData(@Nullable Map<Object, Object> contextData) {
        this.contextData.clear();

        if (!MapUtils.isEmpty(contextData)) {
            this.contextData.putAll(contextData);
        }

        return ((T) this);
    }

    @Override
    public boolean hasContextNode() {
        return (this.contextNode != null);
    }

    @Nullable
    @Override
    public XdmNode getContextNode() {
        return this.contextNode;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setContextNode(@Nullable XdmNode contextNode) {
        this.contextNode = contextNode;

        return ((T) this);
    }
}
