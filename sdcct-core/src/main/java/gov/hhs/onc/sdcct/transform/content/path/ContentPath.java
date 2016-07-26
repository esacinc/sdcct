package gov.hhs.onc.sdcct.transform.content.path;

import java.util.LinkedList;
import java.util.Map;

public interface ContentPath {
    public String getFluentPathExpression();

    public String getJsonPointerExpression();

    public Map<String, String> getNamespaces();

    public LinkedList<ContentPathSegment<?, ?>> getSegments();

    public String getXpathExpression();
}
