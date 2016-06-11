package gov.hhs.onc.sdcct.data.db.server;

import gov.hhs.onc.sdcct.beans.LifecycleBean;
import gov.hhs.onc.sdcct.data.db.security.impl.DbUser;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import java.net.InetAddress;
import javax.annotation.Nonnegative;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

public interface DbServer extends DisposableBean, InitializingBean, LifecycleBean {
    public DbUser getAdminUser();

    public void setAdminUser(DbUser adminUser);

    public String getDatabaseName();

    public void setDatabaseName(String dbName);

    public InetAddress getHostAddress();

    public void setHostAddress(InetAddress hostAddr);

    public ResourceSource[] getInitializationScriptSources();

    public void setInitializationScriptSources(ResourceSource ... initScriptSrcs);

    @Nonnegative
    public int getPort();

    public void setPort(@Nonnegative int port);

    public CustomizableThreadFactory getThreadFactory();

    public void setThreadFactory(CustomizableThreadFactory threadFactory);
}
