package gov.hhs.onc.sdcct.data.db.impl;

import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
import gov.hhs.onc.sdcct.beans.factory.impl.EmbeddedPlaceholderResolver;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.net.utils.SdcctUriUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import javax.annotation.Nonnegative;
import org.hsqldb.DatabaseURL;
import org.hsqldb.jdbc.JDBCDriver;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ResourceUtils;

public class SdcctDataSource extends AbstractComboPooledDataSource implements DisposableBean, InitializingBean {
    private static class SdcctHsqlServer extends Server {
        {
            this.setDaemon(true);
            this.setSilent(true);
        }

        @Override
        protected void printStackTrace(Throwable throwable) {
        }

        @Override
        protected void printError(String msg) {
            LOGGER.error(msg);
        }

        @Override
        protected void print(String msg) {
            LOGGER.debug(msg);
        }
    }

    private final static String DB_PATH_FORMAT_STR = ResourceUtils.FILE_URL_PREFIX + "%s/%s;user=%s;password=%s";

    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctDataSource.class);

    private final static long serialVersionUID = 0L;

    @Autowired
    private EmbeddedPlaceholderResolver embeddedPlaceholderResolver;

    private File dbDir;
    private String dbServerHost;
    private int dbServerPort;
    private ResourceSource[] dbInitScriptSrcs;
    private String dbName;
    private SdcctHsqlServer server = new SdcctHsqlServer();

    @Override
    public void destroy() throws Exception {
        this.server.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.server.setAddress(this.dbServerHost);
        this.server.setDatabaseName(0, this.dbName);
        this.server.setDatabasePath(0, String.format(DB_PATH_FORMAT_STR, this.dbDir.getPath(), this.dbName, this.getUser(), this.getPassword()));
        this.server.setPort(this.dbServerPort);

        this.setDriverClass(JDBCDriver.class.getName());

        this.setJdbcUrl((DatabaseURL.S_URL_PREFIX + DatabaseURL.S_HSQL + this.dbServerHost + SdcctStringUtils.COLON_CHAR + this.dbServerPort
            + SdcctUriUtils.PATH_DELIM + this.dbName));

        boolean initDb = (!this.dbDir.exists() || (this.dbDir.list().length == 0));

        this.server.start();

        if (initDb) {
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

    public boolean isRunning() {
        return (this.server.getState() == ServerConstants.SERVER_STATE_ONLINE);
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

    public String getDatabaseServerHost() {
        return this.dbServerHost;
    }

    public void setDatabaseServerHost(String dbServerHost) {
        this.dbServerHost = dbServerHost;
    }

    @Nonnegative
    public int getDatabaseServerPort() {
        return this.dbServerPort;
    }

    public void setDatabaseServerPort(@Nonnegative int dbServerPort) {
        this.dbServerPort = dbServerPort;
    }
}
