package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.data.metadata.MetadataComponent;

public abstract class AbstractMetadataComponent implements MetadataComponent {
    protected String name;

    protected AbstractMetadataComponent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
