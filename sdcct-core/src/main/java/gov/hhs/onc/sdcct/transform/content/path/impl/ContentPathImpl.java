package gov.hhs.onc.sdcct.transform.content.path.impl;

import gov.hhs.onc.sdcct.transform.content.path.ContentPath;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathSegment;
import java.util.LinkedList;
import java.util.Map;

public class ContentPathImpl implements ContentPath {
    private final static String TO_STR_FORMAT = "fluentPathExpr=%s, jsonPointerExpr=%s, xpathExpr=%s";

    private Map<String, String> namespaces;
    private LinkedList<ContentPathSegment<?, ?>> segments;
    private String fluentPathExpr;
    private String jsonPointerExpr;
    private String xpathExpr;

    public ContentPathImpl(Map<String, String> namespaces, LinkedList<ContentPathSegment<?, ?>> segments, String fluentPathExpr, String jsonPointerExpr,
        String xpathExpr) {
        this.namespaces = namespaces;
        this.segments = segments;
        this.fluentPathExpr = fluentPathExpr;
        this.jsonPointerExpr = jsonPointerExpr;
        this.xpathExpr = xpathExpr;
    }

    @Override
    public String toString() {
        return String.format(TO_STR_FORMAT, this.fluentPathExpr, this.jsonPointerExpr, this.xpathExpr);
    }

    @Override
    public String getFluentPathExpression() {
        return this.fluentPathExpr;
    }

    @Override
    public String getJsonPointerExpression() {
        return this.jsonPointerExpr;
    }

    @Override
    public Map<String, String> getNamespaces() {
        return this.namespaces;
    }

    @Override
    public LinkedList<ContentPathSegment<?, ?>> getSegments() {
        return this.segments;
    }

    @Override
    public String getXpathExpression() {
        return this.xpathExpr;
    }
}
