package gov.hhs.onc.sdcct.data.db.impl;

import ch.qos.logback.ext.spring.ApplicationContextHolder;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbService;
import gov.hhs.onc.sdcct.data.db.DbServiceContributor;
import java.util.Map;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.spi.ServiceRegistryImplementor;

public abstract class AbstractDbServiceContributor<T extends DbService> implements DbServiceContributor<T> {
    protected Class<T> serviceClass;

    protected AbstractDbServiceContributor(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public T initiateService(Map configValues, ServiceRegistryImplementor serviceRegistry) {
        return ApplicationContextHolder.getApplicationContext().getBean(this.serviceClass);
    }

    @Override
    public void contribute(StandardServiceRegistryBuilder serviceRegistryBuilder) {
        serviceRegistryBuilder.addInitiator(this);
    }

    @Override
    public Class<T> getServiceInitiated() {
        return this.serviceClass;
    }
}
