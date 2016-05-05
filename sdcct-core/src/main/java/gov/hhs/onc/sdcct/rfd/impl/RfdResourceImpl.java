package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.beans.SpecificationNames;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Cache(region = DbTableNames.RESOURCE, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Cacheable
@DiscriminatorValue(SpecificationNames.RFD)
@Entity(name = "resourceRfd")
public class RfdResourceImpl extends SdcctResourceImpl implements RfdResource {
    private final static long serialVersionUID = 0L;

    public RfdResourceImpl() {
        this.specType = SpecificationType.RFD;
    }
}
