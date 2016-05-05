package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctRegistry;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;

public class FhirResourceRegistryImpl<T extends DomainResource> extends AbstractSdcctRegistry<T, FhirResource> implements FhirResourceRegistry<T> {
    public FhirResourceRegistryImpl(Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(beanClass, beanImplClass, FhirResource.class, FhirResourceImpl.class, FhirResourceImpl::new);
    }

    @Override
    protected FhirResource encode(T bean) throws Exception {
        FhirResource entity = super.encode(bean);
        // noinspection ConstantConditions
        entity.setType(bean.getTypeId());

        return entity;
    }
}
