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
import org.hsqldb.server.ServerConfiguration;
import org.hsqldb.server.ServerConstants;
import org.hsqldb.server.ServerProperties;
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
        @Override
        protected void printStackTrace(Throwable throwable) {
        }

        @Override
        protected void printError(String msg) {
            LOGGER.error(msg, this.getServerError());
        }

        @Override
        protected void print(String msg) {
            LOGGER.debug(msg);
        }
    }

    private final static String DB_SERVER_MAX_CONN_PROP_NAME = "server.maxconnections";

    private final static String DB_PATH_FORMAT_STR = ResourceUtils.FILE_URL_PREFIX + "%s/%s;user=%s;password=%s";

    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctDataSource.class);

    private final static long serialVersionUID = 0L;

    @Autowired
    private EmbeddedPlaceholderResolver embeddedPlaceholderResolver;

    private String adminUserName;
    private String adminUserPass;
    private File dbDir;
    private String dbServerHostAddr;
    private int dbServerPort;
    private ResourceSource[] dbInitScriptSrcs;
    private String dbName;
    private String mainUserName;
    private String mainUserPass;
    private SdcctHsqlServer server = new SdcctHsqlServer();

    @Override
    public void destroy() throws Exception {
        this.server.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerProperties serverProps = ServerConfiguration.newDefaultProperties(ServerConstants.SC_PROTOCOL_HSQL);
        serverProps.getProperties().clear();
        serverProps.setProperty(DB_SERVER_MAX_CONN_PROP_NAME, this.getMaxPoolSize());
        this.server.setProperties(serverProps);

        this.server.setAddress(this.dbServerHostAddr);
        this.server.setDatabaseName(0, this.dbName);
        this.server.setDatabasePath(0, String.format(DB_PATH_FORMAT_STR, this.dbDir.getPath(), this.dbName, this.adminUserName, this.adminUserPass));
        this.server.setDaemon(true);
        this.server.setPort(this.dbServerPort);

        this.setDriverClass(JDBCDriver.class.getName());

        this.setJdbcUrl((DatabaseURL.S_URL_PREFIX + DatabaseURL.S_HSQL + this.dbServerHostAddr + SdcctStringUtils.COLON_CHAR + this.dbServerPort
            + SdcctUriUtils.PATH_DELIM + this.dbName));

        boolean initDb = (!this.dbDir.exists() || (this.dbDir.list().length == 0));

        this.server.start();

        if (initDb) {
            Connection conn = null;

            try {
                super.setUser(this.adminUserName);
                super.setPassword(this.adminUserPass);

                conn = DataSourceUtils.getConnection(this);

                for (ResourceSource dbInitScriptSrc : this.dbInitScriptSrcs) {
                    ScriptUtils.executeSqlScript(
                        conn,
                        new EncodedResource(new ByteArrayResource(this.embeddedPlaceholderResolver.resolvePlaceholders(
                            new String(dbInitScriptSrc.getBytes(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8), dbInitScriptSrc.getResource()
                            .getDescription()), StandardCharsets.UTF_8));
                }
            } finally {
                super.setUser(this.mainUserName);
                super.setPassword(this.mainUserPass);

                if (conn != null) {
                    DataSourceUtils.releaseConnection(conn, this);
                }

                this.resetPoolManager(true);
            }
        }
    }

    public boolean isRunning() {
        return (this.server.getState() == ServerConstants.SERVER_STATE_ONLINE);
    }

    public String getAdminUser() {
        return this.adminUserName;
    }

    public void setAdminUser(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword() {
        return this.adminUserPass;
    }

    public void setAdminPassword(String adminUserPass) {
        this.adminUserPass = adminUserPass;
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

    public String getDatabaseServerHostAddress() {
        return this.dbServerHostAddr;
    }

    public void setDatabaseServerHostAddress(String dbServerHostAddr) {
        this.dbServerHostAddr = dbServerHostAddr;
    }

    @Nonnegative
    public int getDatabaseServerPort() {
        return this.dbServerPort;
    }

    public void setDatabaseServerPort(@Nonnegative int dbServerPort) {
        this.dbServerPort = dbServerPort;
    }

    @Override
    public void setPassword(String pass) {
        super.setPassword((this.mainUserPass = pass));
    }

    @Override
    public void setUser(String user) {
        super.setUser((this.mainUserName = user));
    }
}
