package gov.hhs.onc.sdcct.transform.content.path;

import java.util.LinkedList;
import org.apache.commons.collections4.BidiMap;

public interface ContentPath {
    public String getFluentPathExpression();

    public String getJsonPointerExpression();

    public BidiMap<String, String> getNamespaces();

    public LinkedList<ContentPathSegment<?, ?>> getSegments();

    public String getXpathExpression();
}
