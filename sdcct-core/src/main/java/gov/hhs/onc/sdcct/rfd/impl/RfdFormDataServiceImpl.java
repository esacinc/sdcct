package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDataService;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.rfd.RfdFormDao;
import gov.hhs.onc.sdcct.rfd.RfdFormDataService;
import org.springframework.stereotype.Service;

@Service("dataServiceFormRfd")
public class RfdFormDataServiceImpl extends AbstractSdcctDataService<RfdForm, RfdFormDao> implements RfdFormDataService {
    public RfdFormDataServiceImpl() {
        super(RfdForm.class, RfdFormImpl.class);
    }
}
