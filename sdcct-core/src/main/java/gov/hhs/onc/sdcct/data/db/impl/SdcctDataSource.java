package gov.hhs.onc.sdcct.data.db.impl;

import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import gov.hhs.onc.sdcct.beans.factory.impl.EmbeddedPlaceholderResolver;
import gov.hhs.onc.sdcct.config.utils.SdcctPropertiesUtils;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.derby.iapi.reference.Attribute;
import org.apache.derby.iapi.reference.Property;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.apache.derby.shared.common.reference.SQLState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.embedded.OutputStreamFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

public class SdcctDataSource extends AbstractComboPooledDataSource implements DisposableBean, InitializingBean {
    private final static String NO_OP_ERROR_LOG_METHOD_PROP_VALUE =
        (OutputStreamFactory.class.getName() + SdcctStringUtils.PERIOD_CHAR + OutputStreamFactory.class.getDeclaredMethods()[0].getName());

    private final static Properties CREATE_PROPS = SdcctPropertiesUtils.singleton(Attribute.CREATE_ATTR, Boolean.toString(true));

    private final static Properties SHUTDOWN_PROPS = Stream.of(new ImmutablePair<>(Attribute.DEREGISTER_ATTR, Boolean.toString(true)),
        new ImmutablePair<>(Attribute.SHUTDOWN_ATTR, Boolean.toString(true))).collect(SdcctPropertiesUtils.toProperties());
    private final static Set<String> SHUTDOWN_IGNORE_SQL_STATES = Stream.of(SQLState.DATABASE_NOT_FOUND, SQLState.SHUTDOWN_DATABASE)
        .map(sqlState -> StringUtils.split(sqlState, SdcctStringUtils.PERIOD, 2)[0]).collect(Collectors.toSet());

    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctDataSource.class);

    private final static long serialVersionUID = 0L;

    @Autowired
    private EmbeddedPlaceholderResolver embeddedPlaceholderResolver;

    private File dbDir;
    private ResourceSource[] dbInitScriptSrcs;
    private String dbName;

    @Override
    public void destroy() throws Exception {
        try {
            new EmbeddedDriver().connect(this.getJdbcUrl(), SHUTDOWN_PROPS);

            LOGGER.info(String.format("Shutdown and de-registered Derby database (name=%s, dir=%s).", this.dbName, this.dbDir.getPath()));
        } catch (SQLException e) {
            if (!SHUTDOWN_IGNORE_SQL_STATES.contains(e.getSQLState())) {
                LOGGER.warn(String.format("Unable to shutdown and de-register Derby database (name=%s, dir=%s).", this.dbName, this.dbDir.getPath()), e);
            }
        } finally {
            DataSources.destroy(this);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty(Property.ERRORLOG_METHOD_PROPERTY, NO_OP_ERROR_LOG_METHOD_PROP_VALUE);

        this.setDriverClass(EmbeddedDriver.class.getName());

        String dbDirPath = this.dbDir.getPath();

        this.setJdbcUrl((Attribute.PROTOCOL + dbDirPath));

        this.setProperties(CREATE_PROPS);

        if (!this.dbDir.exists() || (this.dbDir.list().length == 0)) {
            Connection conn = null;

            try {
                conn = DataSourceUtils.getConnection(this);

                for (ResourceSource dbInitScriptSrc : this.dbInitScriptSrcs) {
                    ScriptUtils.executeSqlScript(
                        conn,
                        new EncodedResource(new ByteArrayResource(this.embeddedPlaceholderResolver.resolvePlaceholders(
                            new String(dbInitScriptSrc.getBytes(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8), dbInitScriptSrc.getResource()
                            .getDescription()), StandardCharsets.UTF_8));
                }
            } finally {
                if (conn != null) {
                    DataSourceUtils.releaseConnection(conn, this);
                }
            }
        }
    }

    public File getDatabaseDirectory() {
        return this.dbDir;
    }

    public void setDatabaseDirectory(File dbDir) {
        this.dbDir = dbDir;
    }

    public ResourceSource[] getDatabaseInitializationScripts() {
        return this.dbInitScriptSrcs;
    }

    public void setDatabaseInitializationScripts(ResourceSource ... dbInitScriptSrcs) {
        this.dbInitScriptSrcs = dbInitScriptSrcs;
    }

    public String getDatabaseName() {
        return this.dbName;
    }

    public void setDatabaseName(String dbName) {
        this.dbName = dbName;
    }
}
