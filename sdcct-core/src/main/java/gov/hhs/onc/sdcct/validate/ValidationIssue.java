package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.api.IssueLevel;
import javax.annotation.Nullable;

public interface ValidationIssue {
    public boolean hasContextXpathExpression();

    @Nullable
    public String getContextXpathExpression();

    public void setContextXpathExpression(@Nullable String contextXpathExpr);

    public IssueLevel getLevel();

    public ValidationLocation getLocation();

    public String getMessage();

    public boolean hasSource();

    @Nullable
    public ValidationSource getSource();

    public boolean hasTestXpathExpression();

    @Nullable
    public String getTestXpathExpression();

    public void setTestXpathExpression(@Nullable String testXpathExpr);

    public ValidationType getType();
}
