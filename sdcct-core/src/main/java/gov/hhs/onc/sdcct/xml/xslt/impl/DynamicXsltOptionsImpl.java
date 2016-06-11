package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.xml.impl.AbstractDynamicXmlTransformOptions;
import gov.hhs.onc.sdcct.xml.xslt.DynamicXsltOptions;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import net.sf.saxon.s9api.Destination;

public class DynamicXsltOptionsImpl extends AbstractDynamicXmlTransformOptions<DynamicXsltOptions> implements DynamicXsltOptions {
    private final static long serialVersionUID = 0L;

    private Destination dest;
    private Source src;

    public DynamicXsltOptionsImpl() {
        super();
    }

    @Override
    protected DynamicXsltOptions cloneInternal() {
        return new DynamicXsltOptionsImpl();
    }

    @Override
    public boolean hasDestination() {
        return (this.dest != null);
    }

    @Nullable
    @Override
    public Destination getDestination() {
        return this.dest;
    }

    @Override
    public DynamicXsltOptions setDestination(@Nullable Destination dest) {
        this.dest = dest;

        return this;
    }

    @Override
    public boolean hasSource() {
        return (this.src != null);
    }

    @Nullable
    @Override
    public Source getSource() {
        return this.src;
    }

    @Override
    public DynamicXsltOptions setSource(@Nullable Source src) {
        this.src = src;

        return this;
    }
}
