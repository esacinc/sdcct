package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadataComponent;
import java.io.Serializable;

public abstract class AbstractJaxbContextMetadataComponent<T extends Serializable> extends AbstractJaxbMetadataComponent
    implements JaxbContextMetadataComponent<T> {
    protected JaxbContextMetadata context;
    protected T expr;

    protected AbstractJaxbContextMetadataComponent(JaxbContextMetadata context, T expr, String name) {
        super(name);

        this.context = context;
        this.expr = expr;
    }

    @Override
    public JaxbContextMetadata getContext() {
        return this.context;
    }

    @Override
    public T getExpression() {
        return this.expr;
    }
}
