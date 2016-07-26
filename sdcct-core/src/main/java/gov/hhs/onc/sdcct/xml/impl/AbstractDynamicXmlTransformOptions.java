package gov.hhs.onc.sdcct.xml.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.xml.DynamicXmlTransformOptions;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;

public abstract class AbstractDynamicXmlTransformOptions<T extends DynamicXmlTransformOptions<T>> extends AbstractXmlTransformOptions<T>
    implements DynamicXmlTransformOptions<T> {
    protected Map<Object, Object> contextData = new HashMap<>();

    private final static long serialVersionUID = 0L;

    protected AbstractDynamicXmlTransformOptions() {
        super();
    }

    @Override
    protected void mergeInternal(T mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.contextData.putAll(mergeOpts.getContextData());
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
}
