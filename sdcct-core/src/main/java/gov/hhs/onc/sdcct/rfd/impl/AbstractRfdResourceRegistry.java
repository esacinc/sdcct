package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctRegistry;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import java.util.function.Supplier;

public abstract class AbstractRfdResourceRegistry<T, U extends RfdResource, V extends SdcctDao<U>, W extends SdcctDataService<U, V>> extends
    AbstractSdcctRegistry<T, U, V, W> implements RfdResourceRegistry<T, U, V, W> {
    protected AbstractRfdResourceRegistry(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass,
        Supplier<U> entityBuilder) {
        super(beanClass, beanImplClass, entityClass, entityImplClass, entityBuilder);
    }
}
