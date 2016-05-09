package gov.hhs.onc.sdcct.data.db.server.impl;

import gov.hhs.onc.sdcct.beans.factory.impl.EmbeddedPlaceholderResolver;
import gov.hhs.onc.sdcct.beans.impl.AbstractLifecycleBean;
import gov.hhs.onc.sdcct.data.db.impl.SdcctHsqlDialect;
import gov.hhs.onc.sdcct.data.db.jdbc.impl.SdcctDataSourceConfig;
import gov.hhs.onc.sdcct.data.db.jdbc.impl.SdcctDriverDataSource;
import gov.hhs.onc.sdcct.data.db.security.DbAuthority;
import gov.hhs.onc.sdcct.data.db.security.impl.DbRole;
import gov.hhs.onc.sdcct.data.db.security.impl.DbUser;
import gov.hhs.onc.sdcct.data.db.server.DbServer;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Map;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.text.StrBuilder;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;

public abstract class AbstractDbServer extends AbstractLifecycleBean implements DbServer {
    @Autowired
    protected SdcctDataSourceConfig dataSrcConfig;

    @Autowired
    protected EmbeddedPlaceholderResolver embeddedPlaceholderResolver;

    protected DbUser adminUser;
    protected String dbName;
    protected InetAddress hostAddr;
    protected ResourceSource[] initScriptSrcs;
    protected int port;

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
        SdcctDriverDataSource adminDataSrc = null;
        Connection adminConn = null;

        try {
            adminConn = DataSourceUtils.getConnection((adminDataSrc = this.dataSrcConfig.buildDataSource(this.adminUser, true)));

            for (ResourceSource initScriptSrc : this.initScriptSrcs) {
                ScriptUtils.executeSqlScript(
                    adminConn,
                    new EncodedResource(new ByteArrayResource(this.embeddedPlaceholderResolver.resolvePlaceholders(
                        new String(initScriptSrc.getBytes(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8), initScriptSrc.getResource()
                        .getDescription()), StandardCharsets.UTF_8));
            }

            DbUser user = this.dataSrcConfig.getUser();
            String userName = user.getName();
            Map<String, DbRole> roles = user.getRoles();
            StrBuilder secSqlBuilder = new StrBuilder();

            secSqlBuilder.append(SdcctHsqlDialect.INSTANCE.getCreateUserString(userName, user.getCredentials()));
            secSqlBuilder.appendNewLine();

            for (String roleName : roles.keySet()) {
                secSqlBuilder.append(SdcctHsqlDialect.INSTANCE.getCreateRoleString(roleName));
                secSqlBuilder.appendNewLine();
                secSqlBuilder.append(SdcctHsqlDialect.INSTANCE.getGrantRoleString(roleName, userName));

                for (DbAuthority authority : roles.get(roleName).getAuthorities()) {
                    secSqlBuilder.appendNewLine();
                    secSqlBuilder.append(SdcctHsqlDialect.INSTANCE.getGrantOnString(authority, roleName));
                }
            }

            ScriptUtils.executeSqlScript(adminConn, new EncodedResource(new ByteArrayResource(secSqlBuilder.build().getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8));

            adminConn.commit();
        } finally {
            if (adminConn != null) {
                DataSourceUtils.releaseConnection(adminConn, adminDataSrc);
            }
        }
    }

    @Override
    public DbUser getAdminUser() {
        return this.adminUser;
    }

    @Override
    public void setAdminUser(DbUser adminUser) {
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

    public InetAddress getHostAddress() {
        return this.hostAddr;
    }

    public void setHostAddress(InetAddress hostAddr) {
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

    @Nonnegative
    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(@Nonnegative int port) {
        this.port = port;
    }
}
