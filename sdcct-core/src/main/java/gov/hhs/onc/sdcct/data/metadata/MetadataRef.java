package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.data.db.DbService;
import org.hibernate.boot.internal.MetadataImpl;

public interface MetadataRef extends DbService {
    public MetadataImpl getMetadata();

    public void setMetadata(MetadataImpl metadata);
}
