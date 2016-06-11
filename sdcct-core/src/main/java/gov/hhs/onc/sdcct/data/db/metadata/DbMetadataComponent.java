package gov.hhs.onc.sdcct.data.db.metadata;

import gov.hhs.onc.sdcct.metadata.MetadataComponent;

public interface DbMetadataComponent extends MetadataComponent {
    public boolean isIndexed();
}
