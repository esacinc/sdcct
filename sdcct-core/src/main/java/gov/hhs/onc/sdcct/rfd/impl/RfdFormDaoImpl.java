package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDao;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.rfd.RfdFormDao;
import org.springframework.stereotype.Repository;

@Repository("daoFormRfd")
public class RfdFormDaoImpl extends AbstractSdcctDao<RfdForm> implements RfdFormDao {
    public RfdFormDaoImpl() {
        super(RfdForm.class, RfdFormImpl.class);
    }
}
