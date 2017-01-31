package gov.hhs.onc.sdcct.xml.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.config.impl.AbstractSdcctOptions;
import gov.hhs.onc.sdcct.xml.XmlTransformOptions;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmValue;
import org.apache.commons.collections4.MapUtils;

public abstract class AbstractXmlTransformOptions<T extends XmlTransformOptions<T>> extends AbstractSdcctOptions<T> implements XmlTransformOptions<T> {
    protected Map<QName, XdmValue> vars = new LinkedHashMap<>();

    private final static long serialVersionUID = 0L;

    protected AbstractXmlTransformOptions() {
        super();
    }

    @Override
    protected void mergeInternal(T mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.vars.putAll(mergeOpts.getVariables());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T addVariable(QName qname, XdmValue value) {
        this.vars.put(qname, value);

        return ((T) this);
    }

    @Override
    public boolean hasVariables() {
        return !this.vars.isEmpty();
    }

    @Override
    public Map<QName, XdmValue> getVariables() {
        return this.vars;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setVariables(@Nullable Map<QName, XdmValue> vars) {
        this.vars.clear();

        if (!MapUtils.isEmpty(vars)) {
            this.vars.putAll(vars);
        }

        return ((T) this);
    }
}
