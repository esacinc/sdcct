package gov.hhs.onc.sdcct.xml;

import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.XdmNode;

public interface DynamicXmlTransformOptions<T extends DynamicXmlTransformOptions<T>> extends XmlTransformOptions<T> {
    public boolean hasContextData();

    public Map<Object, Object> getContextData();

    public T setContextData(@Nullable Map<Object, Object> contextData);

    public boolean hasContextNode();

    @Nullable
    public XdmNode getContextNode();

    public T setContextNode(@Nullable XdmNode contextNode);
}
