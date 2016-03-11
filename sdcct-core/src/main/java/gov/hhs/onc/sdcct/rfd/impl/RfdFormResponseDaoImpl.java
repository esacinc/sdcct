package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDao;
import gov.hhs.onc.sdcct.rfd.RfdFormResponse;
import gov.hhs.onc.sdcct.rfd.RfdFormResponseDao;
import org.springframework.stereotype.Repository;

@Repository("daoFormRespRfd")
public class RfdFormResponseDaoImpl extends AbstractSdcctDao<RfdFormResponse> implements RfdFormResponseDao {
    public RfdFormResponseDaoImpl() {
        super(RfdFormResponse.class, RfdFormResponseImpl.class);
    }
}
