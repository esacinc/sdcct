package gov.hhs.onc.sdcct.transform.content.path;

import java.util.LinkedList;
import java.util.Map;
import net.sf.saxon.om.NodeInfo;
import org.apache.commons.collections4.BidiMap;
import org.springframework.beans.factory.BeanClassLoaderAware;

public interface ContentPathBuilder extends BeanClassLoaderAware {
    public ContentPath build(boolean typed, NodeInfo nodeInfo) throws Exception;

    public ContentPath build(boolean typed, LinkedList<ContentPathSegment<?, ?>> segments) throws Exception;

    public BidiMap<String, String> getNamespaces();

    public void setNamespaces(Map<String, String> namespaces);
}
