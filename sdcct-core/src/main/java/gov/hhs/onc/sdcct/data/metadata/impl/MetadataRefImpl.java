package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractDbService;
import gov.hhs.onc.sdcct.data.db.impl.AbstractDbServiceContributor;
import gov.hhs.onc.sdcct.data.metadata.MetadataRef;
import org.hibernate.boot.internal.MetadataImpl;
import org.springframework.stereotype.Component;

@Component("metadataRef")
public class MetadataRefImpl extends AbstractDbService implements MetadataRef {
    public static class MetadataRefContributor extends AbstractDbServiceContributor<MetadataRef> {
        public MetadataRefContributor() {
            super(MetadataRef.class);
        }
    }

    private final static long serialVersionUID = 0L;

    private MetadataImpl metadata;

    @Override
    public MetadataImpl getMetadata() {
        return this.metadata;
    }

    @Override
    public void setMetadata(MetadataImpl metadata) {
        this.metadata = metadata;
    }
}
