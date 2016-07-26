package gov.hhs.onc.sdcct.data.db.jdbc.impl;

import gov.hhs.onc.sdcct.data.db.DbParamModeType;
import gov.hhs.onc.sdcct.data.db.DbParamNullabilityType;
import gov.hhs.onc.sdcct.data.db.DbStatementType;
import gov.hhs.onc.sdcct.data.db.logging.DbStatementEvent;
import gov.hhs.onc.sdcct.data.db.logging.impl.DbStatementEventImpl;
import gov.hhs.onc.sdcct.data.db.logging.impl.DbParamImpl;
import gov.hhs.onc.sdcct.utils.SdcctAopUtils.SdcctMethodInterceptor;
import gov.hhs.onc.sdcct.utils.SdcctAopUtils.SdcctProxyBuilder;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.utils.SdcctMethodUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.MethodMatchers;
import org.springframework.jdbc.datasource.SmartDataSource;

public class SdcctDriverDataSource implements SmartDataSource {
    private static class StatementMethodInterceptor implements SdcctMethodInterceptor<Statement> {
        private DbStatementEvent event;

        public StatementMethodInterceptor(DbStatementEvent event) {
            this.event = event;
        }

        @Nullable
        @Override
        public Object invoke(MethodInvocation invocation, Method method, String methodName, Object[] args, Statement target) throws Throwable {
            if (StringUtils.startsWith(methodName, SET_METHOD_NAME_PREFIX)) {
                ParameterMetaData paramMetadata = ((PreparedStatement) target).getParameterMetaData();
                int paramIndex = ((int) args[0]), paramPrecision = paramMetadata.getPrecision(paramIndex);
                Object paramValue = null;
                byte[] paramValueBytes;
                String paramValueStr = null;

                if (methodName.equals(SET_NULL_STATEMENT_METHOD_NAME)) {
                    paramValueStr = NULL_STATEMENT_PARAM_VALUE;
                } else if (args[1] instanceof Clob) {
                    paramValue = paramValueStr = ((Clob) args[1]).getSubString(0, paramPrecision);
                } else if (args[1] instanceof Blob) {
                    paramValue = ((Blob) args[1]).getBytes(0, paramPrecision);
                } else if (args[1] instanceof InputStream) {
                    args[1] = new ByteArrayInputStream((paramValueBytes = IOUtils.toByteArray(((InputStream) args[1]))));

                    if (SET_BINARY_STATEMENT_METHOD_NAMES.contains(methodName)) {
                        paramValue = paramValueBytes;
                    } else {
                        paramValue = paramValueStr = new String(paramValueBytes, StandardCharsets.UTF_8);
                    }
                } else if (args[1] instanceof Reader) {
                    args[1] = new InputStreamReader(
                        new ByteArrayInputStream((paramValueBytes = IOUtils.toByteArray(((Reader) args[1]), StandardCharsets.UTF_8))), StandardCharsets.UTF_8);

                    if (SET_BINARY_STATEMENT_METHOD_NAMES.contains(methodName)) {
                        paramValue = paramValueBytes;
                    } else {
                        paramValue = paramValueStr = new String(paramValueBytes, StandardCharsets.UTF_8);
                    }
                } else if (args[1] instanceof byte[]) {
                    paramValue = args[1];
                } else {
                    paramValueStr = (paramValue = args[1]).toString();
                }

                this.event.addParameter(new DbParamImpl((((int) args[0]) - 1), paramMetadata.getParameterType(paramIndex),
                    paramMetadata.getParameterTypeName(paramIndex).toLowerCase(),
                    SdcctEnumUtils.findByOrdinalId(DbParamModeType.class, paramMetadata.getParameterMode(paramIndex)),
                    SdcctEnumUtils.findByOrdinalId(DbParamNullabilityType.class, paramMetadata.isNullable(paramIndex)), paramPrecision,
                    paramMetadata.getScale(paramIndex), paramValue, paramValueStr));
            } else {
                if ((args.length > 0) && (args[0] instanceof String)) {
                    this.event.setSql(((String) args[0]));
                }
            }

            try {
                return method.invoke(target, args);
            } finally {
                if (StringUtils.startsWith(methodName, EXEC_METHOD_NAME_PREFIX)) {
                    LOGGER.trace(this.event.buildMarker(), StringUtils.EMPTY);
                }
            }
        }
    }

    private final static String EXEC_METHOD_NAME_PREFIX = "execute";
    private final static String SET_METHOD_NAME_PREFIX = "set";

    private final static String ADD_BATCH_STATEMENT_METHOD_NAME = "addBatch";
    private final static String SET_NULL_STATEMENT_METHOD_NAME = SET_METHOD_NAME_PREFIX + "Null";

    private final static Set<String> SET_BINARY_STATEMENT_METHOD_NAMES =
        Stream.of((SET_METHOD_NAME_PREFIX + "BinaryStream"), (SET_METHOD_NAME_PREFIX + "Blob")).collect(Collectors.toSet());

    private final static MethodMatcher PROCESS_STATEMENT_METHOD_MATCHER = SdcctMethodUtils
        .matchName(statementMethodName -> StringUtils.startsWithAny(statementMethodName, ADD_BATCH_STATEMENT_METHOD_NAME, EXEC_METHOD_NAME_PREFIX));
    private final static MethodMatcher PROCESS_PARAMS_STATEMENT_METHOD_MATCHER = MethodMatchers.union(PROCESS_STATEMENT_METHOD_MATCHER, SdcctMethodUtils
        .match(statementMethod -> (StringUtils.startsWith(statementMethod.getName(), SET_METHOD_NAME_PREFIX) && (statementMethod.getParameterCount() >= 2))));

    private final static SdcctMethodInterceptor<Connection> CREATE_STATEMENT_CONN_METHOD_INTERCEPTOR =
        (invocation, method, methodName, args, target) -> new SdcctProxyBuilder<>(Statement.class, invocation.proceed())
            .addMethodAdvice(PROCESS_STATEMENT_METHOD_MATCHER, new StatementMethodInterceptor(new DbStatementEventImpl(DbStatementType.STATEMENT, null)))
            .build();

    private final static SdcctMethodInterceptor<Connection> PREPARE_STATEMENT_CONN_METHOD_INTERCEPTOR =
        (invocation, method, methodName, args, target) -> new SdcctProxyBuilder<>(PreparedStatement.class, invocation.proceed())
            .addMethodAdvice(PROCESS_PARAMS_STATEMENT_METHOD_MATCHER, new StatementMethodInterceptor(
                new DbStatementEventImpl(DbStatementType.PREPARED_STATEMENT, (((args.length > 0) && (args[0] instanceof String)) ? ((String) args[0]) : null))))
            .build();

    private final static SdcctMethodInterceptor<Connection> PREPARE_CALL_CONN_METHOD_INTERCEPTOR =
        (invocation, method, methodName, args, target) -> new SdcctProxyBuilder<>(CallableStatement.class, invocation.proceed())
            .addMethodAdvice(PROCESS_PARAMS_STATEMENT_METHOD_MATCHER, new StatementMethodInterceptor(
                new DbStatementEventImpl(DbStatementType.CALLABLE_STATEMENT, (((args.length > 0) && (args[0] instanceof String)) ? ((String) args[0]) : null))))
            .build();

    private final static String NULL_STATEMENT_PARAM_VALUE = "<null>";

    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctDriverDataSource.class);

    private Driver driver;
    private String url;
    private Properties props;
    private String loginTimeoutPropName;
    private boolean singleConn;
    private final Lock connLock;
    private Connection conn;

    public SdcctDriverDataSource(Driver driver, String url, Properties props, String loginTimeoutPropName, boolean singleConn) {
        this.driver = driver;
        this.url = url;
        this.props = props;
        this.loginTimeoutPropName = loginTimeoutPropName;
        this.connLock = ((this.singleConn = singleConn) ? new ReentrantLock() : null);
    }

    @Override
    public boolean shouldClose(Connection conn) {
        return true;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.singleConn) {
            synchronized (this.connLock) {
                if (this.conn == null) {
                    this.conn = this.buildConnection();
                }

                return this.conn;
            }
        } else {
            return this.buildConnection();
        }
    }

    @Override
    public Connection getConnection(String user, String pass) throws SQLException {
        return this.getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        return false;
    }

    private Connection buildConnection() throws SQLException {
        return new SdcctProxyBuilder<>(Connection.class, this.driver.connect(this.url, this.props))
            .addMethodAdvice(SdcctMethodUtils.matchReturnType(Statement.class, false), CREATE_STATEMENT_CONN_METHOD_INTERCEPTOR)
            .addMethodAdvice(SdcctMethodUtils.matchReturnType(PreparedStatement.class, false), PREPARE_STATEMENT_CONN_METHOD_INTERCEPTOR)
            .addMethodAdvice(SdcctMethodUtils.matchReturnType(CallableStatement.class, false), PREPARE_CALL_CONN_METHOD_INTERCEPTOR).build();
    }

    @Nonnegative
    @Override
    public int getLoginTimeout() throws SQLException {
        return (this.props.containsKey(this.loginTimeoutPropName) ? Integer.parseInt(this.props.getProperty(this.loginTimeoutPropName)) : 0);
    }

    @Override
    public void setLoginTimeout(@Nonnegative int loginTimeout) throws SQLException {
        this.props.put(this.loginTimeoutPropName, Integer.toString(loginTimeout));
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }
}
