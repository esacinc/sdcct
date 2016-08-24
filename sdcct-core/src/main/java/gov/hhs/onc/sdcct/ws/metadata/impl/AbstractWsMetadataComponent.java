package gov.hhs.onc.sdcct.ws.metadata.impl;

import gov.hhs.onc.sdcct.metadata.impl.AbstractMetadataComponent;
import gov.hhs.onc.sdcct.ws.metadata.WsMetadataComponent;

public abstract class AbstractWsMetadataComponent extends AbstractMetadataComponent implements WsMetadataComponent {
    protected String id;

    protected AbstractWsMetadataComponent(String id, String name) {
        super(name);

        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
