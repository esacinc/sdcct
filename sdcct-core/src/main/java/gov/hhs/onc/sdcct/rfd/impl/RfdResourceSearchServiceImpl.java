package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.search.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.RfdResourceSearchService;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdResourceSearchServiceImpl<T extends IdentifiedExtensionType> extends AbstractSearchService<T, RfdResource, RfdResourceRegistry<T>> implements
    RfdResourceSearchService<T> {
    public RfdResourceSearchServiceImpl(Class<T> beanClass, Class<? extends T> beanImplClass, RfdResourceRegistry<T> registry) {
        super(beanClass, beanImplClass, RfdResource.class, RfdResourceImpl.class, registry);
    }
}
