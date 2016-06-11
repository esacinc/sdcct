package gov.hhs.onc.sdcct.fhir.parameter;

import gov.hhs.onc.sdcct.data.parameter.ResourceParamProcessor;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;

public interface FhirResourceParamProcessor extends ResourceParamProcessor<Resource, FhirResourceMetadata<?>, FhirResource> {
}
