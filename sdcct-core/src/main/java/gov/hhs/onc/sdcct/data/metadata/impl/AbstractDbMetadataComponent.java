package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.data.metadata.DbMetadataComponent;

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
