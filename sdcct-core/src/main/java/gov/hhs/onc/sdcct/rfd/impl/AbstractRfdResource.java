package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractRfdResource extends AbstractResourceEntity implements RfdResource {
}
