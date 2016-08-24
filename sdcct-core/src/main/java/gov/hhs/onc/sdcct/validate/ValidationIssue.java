package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import javax.annotation.Nullable;

public interface ValidationIssue {
    public boolean hasContextXpathExpression();

    @Nullable
    public String getContextXpathExpression();

    public void setContextXpathExpression(@Nullable String contextXpathExpr);

    public ValidationLocation getLocation();

    public String getMessage();

    public SdcctIssueSeverity getSeverity();

    public boolean hasSource();

    @Nullable
    public ValidationSource getSource();

    public boolean hasTestXpathExpression();

    @Nullable
    public String getTestXpathExpression();

    public void setTestXpathExpression(@Nullable String testXpathExpr);

    public ValidationType getType();
}
