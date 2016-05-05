package gov.hhs.onc.sdcct.xml;

import java.util.Map;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.XdmNode;

public interface DynamicXmlOptions<T extends DynamicXmlOptions<T>> extends XmlOptions<T> {
    public boolean hasContextData();

    public Map<Object, Object> getContextData();

    public T setContextData(@Nullable Map<Object, Object> contextData);

    public boolean hasContextNode();

    @Nullable
    public XdmNode getContextNode();

    public T setContextNode(@Nullable XdmNode contextNode);
}
