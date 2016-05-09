package gov.hhs.onc.sdcct.data.db.server.impl;

import gov.hhs.onc.sdcct.data.db.server.HsqlServer;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConfiguration;
import org.hsqldb.server.ServerConstants;
import org.hsqldb.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class HsqlServerImpl extends AbstractDbServer implements HsqlServer {
    private static class HsqlNetworkServer extends Server {
        @Override
        protected boolean allowConnection(Socket socket) {
            return (super.allowConnection(socket) && ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().isLoopbackAddress());
        }

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

    private final static String SERVER_MAX_CONN_PROP_NAME = "server.maxconnections";

    private final static String PATH_FORMAT_STR = ResourceUtils.FILE_URL_PREFIX + "%s/%s;user=%s;password=%s";

    private final static Logger LOGGER = LoggerFactory.getLogger(HsqlServerImpl.class);

    private File dbDir;
    private HsqlNetworkServer netServer = new HsqlNetworkServer();

    @Override
    public boolean isRunning() {
        return (this.netServer.getState() == ServerConstants.SERVER_STATE_ONLINE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerProperties serverProps = ServerConfiguration.newDefaultProperties(ServerConstants.SC_PROTOCOL_HSQL);
        serverProps.getProperties().clear();
        serverProps.setProperty(SERVER_MAX_CONN_PROP_NAME, this.dataSrcConfig.getMaximumPoolSize());
        this.netServer.setProperties(serverProps);

        this.netServer.setAddress(this.hostAddr.getHostAddress());
        this.netServer.setDatabaseName(0, this.dbName);
        this.netServer.setDatabasePath(0,
            String.format(PATH_FORMAT_STR, this.dbDir.getPath(), this.dbName, this.adminUser.getName(), this.adminUser.getCredentials()));
        this.netServer.setDaemon(true);
        this.netServer.setPort(this.port);

        super.afterPropertiesSet();
    }

    @Override
    protected void stopInternal() throws Exception {
        this.netServer.stop();
    }

    @Override
    protected void startInternal() throws Exception {
        boolean initDb = (!this.dbDir.exists() || (this.dbDir.list().length == 0));

        this.netServer.start();

        if (initDb) {
            this.executeInitializationScripts();
        }
    }

    @Override
    public File getDatabaseDirectory() {
        return this.dbDir;
    }

    @Override
    public void setDatabaseDirectory(File dbDir) {
        this.dbDir = dbDir;
    }
}
