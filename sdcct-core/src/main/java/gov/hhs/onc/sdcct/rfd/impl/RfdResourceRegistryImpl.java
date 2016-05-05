package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.beans.TypeBean;
import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctRegistry;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdResourceRegistryImpl<T extends IdentifiedExtensionType> extends AbstractSdcctRegistry<T, RfdResource> implements RfdResourceRegistry<T> {
    public RfdResourceRegistryImpl(Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(beanClass, beanImplClass, RfdResource.class, RfdResourceImpl.class, RfdResourceImpl::new);
    }

    @Override
    protected RfdResource encode(T bean) throws Exception {
        RfdResource entity = super.encode(bean);
        entity.setType(((TypeBean) bean).getTypeId());

        return entity;
    }
}
