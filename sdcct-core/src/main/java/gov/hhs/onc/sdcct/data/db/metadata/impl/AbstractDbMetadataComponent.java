package gov.hhs.onc.sdcct.data.db.metadata.impl;

import gov.hhs.onc.sdcct.data.db.metadata.DbMetadataComponent;
import gov.hhs.onc.sdcct.metadata.impl.AbstractMetadataComponent;

public abstract class AbstractDbMetadataComponent extends AbstractMetadataComponent implements DbMetadataComponent {
    protected boolean indexed;

    protected AbstractDbMetadataComponent(String name, boolean indexed) {
        super(name);

        this.indexed = indexed;
    }

    @Override
    public boolean isIndexed() {
        return this.indexed;
    }
}
