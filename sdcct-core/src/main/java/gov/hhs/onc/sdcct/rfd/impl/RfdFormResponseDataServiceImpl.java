package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDataService;
import gov.hhs.onc.sdcct.rfd.RfdFormResponse;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseDao;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseDataService;
import org.springframework.stereotype.Service;

@Service("dataServiceFormRespRfd")
public class RfdFormResponseDataServiceImpl extends AbstractSdcctDataService<RfdFormResponse, RfdFormResponseDao> implements RfdFormResponseDataService {
    public RfdFormResponseDataServiceImpl() {
        super(RfdFormResponse.class, RfdFormResponseImpl.class);
    }
}
