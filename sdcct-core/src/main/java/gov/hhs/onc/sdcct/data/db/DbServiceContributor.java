package gov.hhs.onc.sdcct.data.db;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.service.spi.ServiceContributor;

public interface DbServiceContributor<T extends DbService> extends ServiceContributor, StandardServiceInitiator<T> {
}
