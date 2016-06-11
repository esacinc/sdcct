package gov.hhs.onc.sdcct.fhir.metadata.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadata;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.Resource;

public class FhirResourceMetadataImpl<T extends Resource> extends AbstractResourceMetadata<T> implements FhirResourceMetadata<T> {
    public FhirResourceMetadataImpl(String id, String name, String path, Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(SpecificationType.FHIR, id, name, path, beanClass, beanImplClass);
    }
}
