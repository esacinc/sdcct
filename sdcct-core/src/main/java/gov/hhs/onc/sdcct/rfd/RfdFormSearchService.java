package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;

public interface RfdFormSearchService extends SearchService<FormDesignType, RfdForm, RfdFormDao, RfdFormDataService, RfdFormRegistry> {
}
