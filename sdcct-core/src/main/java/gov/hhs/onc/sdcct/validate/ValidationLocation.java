package gov.hhs.onc.sdcct.validate;

import javax.annotation.Nullable;

public interface ValidationLocation {
    public boolean hasColumnNumber();

    public int getColumnNumber();

    public boolean hasFluentPathExpression();

    @Nullable
    public String getFluentPathExpression();

    public void setFluentPathExpression(@Nullable String fluentPathExpr);

    public boolean hasJsonPointerExpression();

    @Nullable
    public String getJsonPointerExpression();

    public void setJsonPointerExpression(@Nullable String jsonPointerExpr);

    public boolean hasLineNumber();

    public int getLineNumber();

    public boolean hasXpathExpression();

    @Nullable
    public String getXpathExpression();

    public void setXpathExpression(@Nullable String xpathExpr);
}
