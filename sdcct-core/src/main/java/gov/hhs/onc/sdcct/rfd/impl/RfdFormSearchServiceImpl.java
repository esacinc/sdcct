package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.rfd.RfdFormDao;
import gov.hhs.onc.sdcct.rfd.RfdFormDataService;
import gov.hhs.onc.sdcct.rfd.RfdFormRegistry;
import gov.hhs.onc.sdcct.rfd.RfdFormSearchService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import org.springframework.stereotype.Component;

@Component("searchServiceFormRfd")
public class RfdFormSearchServiceImpl extends AbstractSearchService<FormDesignType, RfdForm, RfdFormDao, RfdFormDataService, RfdFormRegistry> implements
    RfdFormSearchService {
    public RfdFormSearchServiceImpl() {
        super(FormDesignType.class, FormDesignTypeImpl.class, RfdForm.class, RfdFormImpl.class);
    }
}
