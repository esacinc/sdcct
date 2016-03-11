package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.rfd.RfdFormResponse;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseDao;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseDataService;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseRegistry;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseSearchService;
import gov.hhs.onc.sdcct.sdc.FormResponsesType;
import gov.hhs.onc.sdcct.sdc.impl.FormResponsesTypeImpl;
import org.springframework.stereotype.Component;

@Component("searchServiceFormRespRfd")
public class RfdFormResponseSearchServiceImpl extends
    AbstractSearchService<FormResponsesType, RfdFormResponse, RfdFormResponseDao, RfdFormResponseDataService, RfdFormResponseRegistry> implements
    RfdFormResponseSearchService {
    public RfdFormResponseSearchServiceImpl() {
        super(FormResponsesType.class, FormResponsesTypeImpl.class, RfdFormResponse.class, RfdFormResponseImpl.class);
    }
}
