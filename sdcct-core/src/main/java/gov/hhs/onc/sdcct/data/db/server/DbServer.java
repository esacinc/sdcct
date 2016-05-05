package gov.hhs.onc.sdcct.data.db.server;

import gov.hhs.onc.sdcct.beans.LifecycleBean;
import gov.hhs.onc.sdcct.data.db.impl.SdcctDataSource;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import javax.annotation.Nonnegative;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public interface DbServer extends DisposableBean, InitializingBean, LifecycleBean {
    public String getAdminPassword();

    public void setAdminPassword(String adminPass);

    public String getAdminUser();

    public void setAdminUser(String adminUser);

    public String getDatabaseName();

    public void setDatabaseName(String dbName);

    public SdcctDataSource getDataSource();

    public String getHostAddress();

    public void setHostAddress(String hostAddr);

    public ResourceSource[] getInitializationScriptSources();

    public void setInitializationScriptSources(ResourceSource ... initScriptSrcs);

    public String getPassword();

    public void setPassword(String pass);

    @Nonnegative
    public int getPort();

    public void setPort(@Nonnegative int port);

    public String getUser();

    public void setUser(String user);
}
