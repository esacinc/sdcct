package gov.hhs.onc.sdcct.data.db.server.impl;

import gov.hhs.onc.sdcct.beans.factory.impl.EmbeddedPlaceholderResolver;
import gov.hhs.onc.sdcct.beans.impl.AbstractLifecycleBean;
import gov.hhs.onc.sdcct.data.db.impl.SdcctDataSource;
import gov.hhs.onc.sdcct.data.db.server.DbServer;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import javax.annotation.Nonnegative;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;

public abstract class AbstractDbServer extends AbstractLifecycleBean implements DbServer {
    @Autowired
    protected SdcctDataSource dataSrc;

    @Autowired
    protected EmbeddedPlaceholderResolver embeddedPlaceholderResolver;

    protected String adminPass;
    protected String adminUser;
    protected String dbName;
    protected String hostAddr;
    protected ResourceSource[] initScriptSrcs;
    protected String pass;
    protected int port;
    protected String user;

    @Override
    public void stop() {
        if (this.isRunning()) {
            try {
                this.stopInternal();
            } catch (Exception e) {
                throw new HibernateException(String.format("Unable to stop database (name=%s) server (hostAddr=%s, port=%d).", this.dbName, this.hostAddr,
                    this.port), e);
            }
        }
    }

    @Override
    public void start() {
        if (!this.isRunning()) {
            try {
                this.startInternal();
            } catch (Exception e) {
                throw new HibernateException(String.format("Unable to start database (name=%s) server (hostAddr=%s, port=%d).", this.dbName, this.hostAddr,
                    this.port), e);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }

    protected abstract void stopInternal() throws Exception;

    protected abstract void startInternal() throws Exception;

    protected void executeInitializationScripts() throws Exception {
        Connection conn = null;

        try {
            this.dataSrc.setUser(this.adminUser);
            this.dataSrc.setPassword(this.adminPass);

            conn = DataSourceUtils.getConnection(this.dataSrc);

            for (ResourceSource initScriptSrc : this.initScriptSrcs) {
                ScriptUtils.executeSqlScript(
                    conn,
                    new EncodedResource(new ByteArrayResource(this.embeddedPlaceholderResolver.resolvePlaceholders(
                        new String(initScriptSrc.getBytes(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8), initScriptSrc.getResource()
                        .getDescription()), StandardCharsets.UTF_8));
            }
        } finally {
            this.dataSrc.setUser(this.user);
            this.dataSrc.setPassword(this.pass);

            if (conn != null) {
                DataSourceUtils.releaseConnection(conn, this.dataSrc);
            }

            this.dataSrc.resetPoolManager(true);
        }
    }

    @Override
    public String getAdminPassword() {
        return this.adminPass;
    }

    @Override
    public void setAdminPassword(String adminPass) {
        this.adminPass = adminPass;
    }

    @Override
    public String getAdminUser() {
        return this.adminUser;
    }

    @Override
    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    @Override
    public String getDatabaseName() {
        return this.dbName;
    }

    @Override
    public void setDatabaseName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public SdcctDataSource getDataSource() {
        return this.dataSrc;
    }

    public String getHostAddress() {
        return this.hostAddr;
    }

    public void setHostAddress(String hostAddr) {
        this.hostAddr = hostAddr;
    }

    @Override
    public ResourceSource[] getInitializationScriptSources() {
        return this.initScriptSrcs;
    }

    @Override
    public void setInitializationScriptSources(ResourceSource ... initScriptSrcs) {
        this.initScriptSrcs = initScriptSrcs;
    }

    @Override
    public String getPassword() {
        return this.pass;
    }

    @Override
    public void setPassword(String pass) {
        this.pass = pass;
    }

    @Nonnegative
    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(@Nonnegative int port) {
        this.port = port;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }
}
