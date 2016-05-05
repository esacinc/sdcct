package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadata;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.ResourceType;
import java.net.URI;

public class FhirResourceMetadataImpl<T extends DomainResource> extends AbstractResourceMetadata<ResourceType, T> implements FhirResourceMetadata<T> {
    public FhirResourceMetadataImpl(String id, String name, URI uri, ResourceType type, Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(SpecificationType.FHIR, id, name, uri, type, beanClass, beanImplClass);
    }
}
