package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.event.impl.AbstractResourceDbEventListener;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceDbEventListener;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.RfdResourceProcessor;
import gov.hhs.onc.sdcct.rfd.RfdResourceType;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdResourceDbEventListenerImpl
    extends
    AbstractResourceDbEventListener<RfdResourceType, IdentifiedExtensionType, RfdResource, RfdResourceMetadata<? extends IdentifiedExtensionType>, RfdResourceProcessor>
    implements RfdResourceDbEventListener {
    private final static long serialVersionUID = 0L;

    public RfdResourceDbEventListenerImpl() {
        super(RfdResourceType.class, RfdResource.class, RfdResourceImpl.class);
    }
}
