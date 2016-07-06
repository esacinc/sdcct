package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.transform.content.path.ContentPath;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringStyle;
import gov.hhs.onc.sdcct.validate.ValidationLocation;
import javax.annotation.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ValidationLocationImpl implements ValidationLocation {
    private int colNum;
    private int lineNum;
    private String fluentPathExpr;
    private String jsonPointerExpr;
    private String xpathExpr;

    public ValidationLocationImpl() {
        this(-1, -1);
    }

    public ValidationLocationImpl(SdcctLocation loc) {
        // noinspection ConstantConditions
        this(loc.getColumnNumber(), loc.getLineNumber());

        if (loc.hasContentPath()) {
            ContentPath locContentPath = loc.getContentPath();

            // noinspection ConstantConditions
            this.fluentPathExpr = locContentPath.getFluentPathExpression();
            this.jsonPointerExpr = locContentPath.getJsonPointerExpression();
            this.xpathExpr = locContentPath.getXpathExpression();
        }
    }

    public ValidationLocationImpl(int colNum, int lineNum) {
        this.colNum = colNum;
        this.lineNum = lineNum;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(null, SdcctToStringStyle.INSTANCE);
        builder.append("colNum", this.colNum);
        builder.append("lineNum", this.lineNum);

        if (this.hasFluentPathExpression()) {
            builder.append("fluentXpathExpr", this.fluentPathExpr);
        }

        if (this.hasJsonPointerExpression()) {
            builder.append("jsonPointerExpr", this.jsonPointerExpr);
        }

        if (this.hasXpathExpression()) {
            builder.append("xpathExpr", this.xpathExpr);
        }

        return builder.build();
    }

    public boolean hasColumnNumber() {
        return (this.colNum >= 0);
    }

    @Override
    public int getColumnNumber() {
        return this.colNum;
    }

    @Override
    public boolean hasFluentPathExpression() {
        return (this.fluentPathExpr != null);
    }

    @Nullable
    @Override
    public String getFluentPathExpression() {
        return this.fluentPathExpr;
    }

    @Override
    public void setFluentPathExpression(@Nullable String fluentPathExpr) {
        this.fluentPathExpr = fluentPathExpr;
    }

    @Override
    public boolean hasJsonPointerExpression() {
        return (this.jsonPointerExpr != null);
    }

    @Nullable
    @Override
    public String getJsonPointerExpression() {
        return this.jsonPointerExpr;
    }

    @Override
    public void setJsonPointerExpression(@Nullable String jsonPointerExpr) {
        this.jsonPointerExpr = jsonPointerExpr;
    }

    public boolean hasLineNumber() {
        return (this.lineNum >= 0);
    }

    @Override
    public int getLineNumber() {
        return this.lineNum;
    }

    @Override
    public boolean hasXpathExpression() {
        return (this.xpathExpr != null);
    }

    @Nullable
    @Override
    public String getXpathExpression() {
        return this.xpathExpr;
    }

    @Override
    public void setXpathExpression(@Nullable String xpathExpr) {
        this.xpathExpr = xpathExpr;
    }
}
