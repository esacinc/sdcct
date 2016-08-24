package gov.hhs.onc.sdcct.rfd.ws;

import gov.hhs.onc.sdcct.form.ws.FormWebService;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.search.RfdSearchService;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdInteractionWsMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdResourceWsMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdWsMetadata;

public interface RfdFormWebService
    extends FormWebService<RfdResource, RfdResourceRegistry<?>, RfdSearchService<?>, RfdInteractionWsMetadata, RfdResourceWsMetadata<?>, RfdWsMetadata> {
}
