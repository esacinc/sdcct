package gov.hhs.onc.sdcct.fhir.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.db.impl.AbstractDbBeanDefinitionRegistryPostProcessor;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.FhirResourceSearchService;
import gov.hhs.onc.sdcct.fhir.ResourceType;
import org.springframework.stereotype.Component;

@Component("beanDefRegistryPostProcDbFhir")
public class FhirDbBeanDefinitionRegistryPostProcessor
    extends
    AbstractDbBeanDefinitionRegistryPostProcessor<ResourceType, DomainResource, FhirResource, FhirResourceRegistry<DomainResource>, FhirResourceSearchService<DomainResource>> {
    @SuppressWarnings({ CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED })
    public FhirDbBeanDefinitionRegistryPostProcessor() {
        super(SpecificationType.FHIR, ResourceType.class, DomainResource.class, DomainResourceImpl.class, FhirResource.class,
            ((Class<FhirResourceRegistry<DomainResource>>) ((Class<? extends FhirResourceRegistry>) FhirResourceRegistry.class)),
            ((Class<FhirResourceRegistryImpl<DomainResource>>) ((Class<? extends FhirResourceRegistryImpl>) FhirResourceRegistryImpl.class)),
            ((Class<FhirResourceSearchService<DomainResource>>) ((Class<? extends FhirResourceSearchService>) FhirResourceSearchService.class)),
            ((Class<FhirResourceSearchServiceImpl<DomainResource>>) ((Class<? extends FhirResourceSearchServiceImpl>) FhirResourceSearchServiceImpl.class)));
    }
}
