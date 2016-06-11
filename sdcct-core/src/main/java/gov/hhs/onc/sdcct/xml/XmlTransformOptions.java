package gov.hhs.onc.sdcct.xml;

import gov.hhs.onc.sdcct.config.Options;
import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmValue;

public interface XmlTransformOptions<T extends XmlTransformOptions<T>> extends Options<T> {
    public T addVariable(QName qname, XdmValue value);

    public boolean hasVariables();

    public Map<QName, XdmValue> getVariables();

    public T setVariables(@Nullable Map<QName, XdmValue> vars);
}
