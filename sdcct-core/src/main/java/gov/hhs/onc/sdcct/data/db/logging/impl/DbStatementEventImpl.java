package gov.hhs.onc.sdcct.data.db.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbStatementType;
import gov.hhs.onc.sdcct.data.db.logging.DbStatementEvent;
import gov.hhs.onc.sdcct.data.db.logging.DbParam;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DbStatementEventImpl extends AbstractDbEvent implements DbStatementEvent {
    private final static Pattern SQL_PARAM_PLACEHOLDER_PATTERN = Pattern.compile("(?<=[ \\(=])\\?");

    private DbStatementType type;
    private String sql;
    private List<DbParam> params = new ArrayList<>();

    public DbStatementEventImpl(DbStatementType type, @Nullable String sql) {
        this.type = type;
        this.sql = sql;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void buildMarkerMessages(StringBuffer msgBuffer, ToStringBuilder msgToStrBuilder, StringBuffer logstashFileMsgBuffer,
        ToStringBuilder logstashFileMsgToStrBuilder) {
        msgBuffer.append("Database ");
        msgBuffer.append(this.type.getId());
        msgBuffer.append(" executed");

        logstashFileMsgBuffer.append(msgBuffer);
        logstashFileMsgBuffer.append(SdcctStringUtils.PERIOD_CHAR);

        msgBuffer.append(": ");

        if (!this.params.isEmpty()) {
            Matcher paramPlaceholderMatcher = SQL_PARAM_PLACEHOLDER_PATTERN.matcher(this.sql);
            int paramIndex = -1;
            DbParam param;

            while (paramPlaceholderMatcher.find()) {
                paramPlaceholderMatcher.appendReplacement(msgBuffer, SdcctStringUtils.LT);

                msgBuffer.append((param = this.params.get(++paramIndex)).getTypeName());
                msgBuffer.append(SdcctStringUtils.COLON_CHAR);
                msgBuffer.append(param.getPrecision());
                msgBuffer.append(SdcctStringUtils.COLON_CHAR);
                msgBuffer.append(param.getScale());

                if (param.hasValueString()) {
                    msgBuffer.append(SdcctStringUtils.COLON_CHAR);
                    msgBuffer.append(param.getValueString());
                }

                msgBuffer.append(SdcctStringUtils.GT_CHAR);
            }

            paramPlaceholderMatcher.appendTail(msgBuffer);
        } else {
            msgBuffer.append(this.sql);
        }
    }

    @Override
    protected String buildMarkerFieldName() {
        return (super.buildMarkerFieldName() + SdcctStringUtils.PERIOD + "statement");
    }

    @Override
    public void addParameter(DbParam param) {
        int paramIndex = param.getIndex(), numParams = this.params.size();

        if (numParams <= paramIndex) {
            for (int a = 0; a < ((paramIndex - numParams) + 1); a++) {
                this.params.add(null);
            }
        }

        this.params.set(paramIndex, param);
    }

    @Override
    public List<DbParam> getParameters() {
        return this.params;
    }

    @JsonProperty
    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public void setSql(String sql) {
        this.sql = sql;
    }

    @JsonProperty
    @Override
    public DbStatementType getType() {
        return this.type;
    }
}
