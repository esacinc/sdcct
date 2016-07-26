package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringStyle;
import gov.hhs.onc.sdcct.validate.ValidationIssue;
import gov.hhs.onc.sdcct.validate.ValidationLocation;
import gov.hhs.onc.sdcct.validate.ValidationSource;
import gov.hhs.onc.sdcct.validate.ValidationType;
import javax.annotation.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ValidationIssueImpl implements ValidationIssue {
    protected ValidationType type;
    protected IssueLevel level;
    protected String msg;
    protected ValidationLocation loc;
    protected ValidationSource src;
    protected String contextXpathExpr;
    protected String testXpathExpr;

    public ValidationIssueImpl(ValidationType type, IssueLevel level, String msg, ValidationLocation loc, @Nullable ValidationSource src) {
        this.type = type;
        this.level = level;
        this.msg = msg;
        this.loc = loc;
        this.src = src;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(null, SdcctToStringStyle.INSTANCE);
        builder.append("type", this.type.getId());
        builder.append("level", this.level.getId());
        builder.append("loc", String.format("{%s}", this.loc));

        if (this.hasContextXpathExpression()) {
            builder.append("contextXpathExpr", this.contextXpathExpr);
        }

        if (this.hasTestXpathExpression()) {
            builder.append("testXpathExpr", this.testXpathExpr);
        }

        if (this.hasSource()) {
            builder.append("src", String.format("{%s}", this.src));
        }

        builder.append("msg", this.msg);

        return builder.build();
    }

    @Override
    public boolean hasContextXpathExpression() {
        return (this.contextXpathExpr != null);
    }

    @Override
    public String getContextXpathExpression() {
        return this.contextXpathExpr;
    }

    @Override
    public void setContextXpathExpression(@Nullable String contextXpathExpr) {
        this.contextXpathExpr = contextXpathExpr;
    }

    @Override
    public IssueLevel getLevel() {
        return this.level;
    }

    @Override
    public ValidationLocation getLocation() {
        return this.loc;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public boolean hasSource() {
        return (this.src != null);
    }

    @Nullable
    @Override
    public ValidationSource getSource() {
        return this.src;
    }

    @Override
    public boolean hasTestXpathExpression() {
        return (this.testXpathExpr != null);
    }

    @Nullable
    @Override
    public String getTestXpathExpression() {
        return this.testXpathExpr;
    }

    @Override
    public void setTestXpathExpression(@Nullable String testXpathExpr) {
        this.testXpathExpr = testXpathExpr;
    }

    @Override
    public ValidationType getType() {
        return this.type;
    }
}
