package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.rfd.RfdFormResponse;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseDao;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseDataService;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseRegistry;
import gov.hhs.onc.sdcct.sdc.FormResponsesType;
import gov.hhs.onc.sdcct.sdc.impl.FormResponsesTypeImpl;
import org.springframework.stereotype.Component;

@Component("registryFormRespRfd")
public class RfdFormResponseRegistryImpl extends
    AbstractRfdResourceRegistry<FormResponsesType, RfdFormResponse, RfdFormResponseDao, RfdFormResponseDataService> implements RfdFormResponseRegistry {
    public RfdFormResponseRegistryImpl() {
        super(FormResponsesType.class, FormResponsesTypeImpl.class, RfdFormResponse.class, RfdFormResponseImpl.class, RfdFormResponseImpl::new);
    }

    @Override
    protected RfdFormResponse encode(FormResponsesType bean) throws Exception {
        RfdFormResponse entity = super.encode(bean);
        entity.setId(bean.getFormInstanceURI());

        return entity;
    }
}
