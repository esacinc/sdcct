package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.sdc.FormResponsesType;

public interface RfdFormResponseSearchService extends
    SearchService<FormResponsesType, RfdFormResponse, RfdFormResponseDao, RfdFormResponseDataService, RfdFormResponseRegistry> {
}
