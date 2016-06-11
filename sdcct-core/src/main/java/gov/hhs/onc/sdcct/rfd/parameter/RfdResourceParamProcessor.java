package gov.hhs.onc.sdcct.rfd.parameter;

import gov.hhs.onc.sdcct.data.parameter.ResourceParamProcessor;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceParamProcessor extends ResourceParamProcessor<IdentifiedExtensionType, RfdResourceMetadata<?>, RfdResource> {
}
