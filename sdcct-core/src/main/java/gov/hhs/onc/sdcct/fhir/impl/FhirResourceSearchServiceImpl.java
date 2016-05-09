package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.search.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.FhirResourceSearchService;

public class FhirResourceSearchServiceImpl<T extends DomainResource> extends AbstractSearchService<T, FhirResource, FhirResourceRegistry<T>> implements
    FhirResourceSearchService<T> {
    public FhirResourceSearchServiceImpl(Class<T> beanClass, Class<? extends T> beanImplClass, FhirResourceRegistry<T> registry) {
        super(beanClass, beanImplClass, FhirResource.class, FhirResourceImpl.class, registry);
    }
}
