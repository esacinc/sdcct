package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.rfd.RfdFormDao;
import gov.hhs.onc.sdcct.rfd.RfdFormDataService;
import gov.hhs.onc.sdcct.rfd.RfdFormRegistry;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import org.springframework.stereotype.Component;

@Component("registryFormRfd")
public class RfdFormRegistryImpl extends AbstractRfdResourceRegistry<FormDesignType, RfdForm, RfdFormDao, RfdFormDataService> implements RfdFormRegistry {
    public RfdFormRegistryImpl() {
        super(FormDesignType.class, FormDesignTypeImpl.class, RfdForm.class, RfdFormImpl.class, RfdFormImpl::new);
    }

    @Override
    protected RfdForm encode(FormDesignType bean) throws Exception {
        RfdForm entity = super.encode(bean);
        entity.setId(bean.getFormID());

        return entity;
    }
}
