package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.event.impl.AbstractResourceDbEventListener;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceDbEventListener;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.FhirResourceProcessor;
import gov.hhs.onc.sdcct.fhir.ResourceType;

public class FhirResourceDbEventListenerImpl extends
    AbstractResourceDbEventListener<ResourceType, DomainResource, FhirResource, FhirResourceMetadata<? extends DomainResource>, FhirResourceProcessor>
    implements FhirResourceDbEventListener {
    private final static long serialVersionUID = 0L;

    public FhirResourceDbEventListenerImpl() {
        super(ResourceType.class, FhirResource.class, FhirResourceImpl.class);
    }
}
