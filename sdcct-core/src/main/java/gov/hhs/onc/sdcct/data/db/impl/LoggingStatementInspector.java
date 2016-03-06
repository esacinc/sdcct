package gov.hhs.onc.sdcct.data.db.impl;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("statementInspectorLogging")
public class LoggingStatementInspector implements StatementInspector {
    private final static long serialVersionUID = 0L;

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingStatementInspector.class);

    @Override
    public String inspect(String sql) {
        LOGGER.trace(String.format("SQL statement: %s", sql));

        return sql;
    }
}
