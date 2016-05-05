package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.xml.impl.AbstractDynamicXmlOptions;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import net.sf.saxon.s9api.Destination;

public class DynamicXsltOptions extends AbstractDynamicXmlOptions<DynamicXsltOptions> {
    private final static long serialVersionUID = 0L;

    private Destination dest;
    private Source src;

    public DynamicXsltOptions() {
        super(DynamicXsltOptions::new);
    }

    public boolean hasDestination() {
        return (this.dest != null);
    }

    @Nullable
    public Destination getDestination() {
        return this.dest;
    }

    public DynamicXsltOptions setDestination(@Nullable Destination dest) {
        this.dest = dest;

        return this;
    }

    public boolean hasSource() {
        return (this.src != null);
    }

    @Nullable
    public Source getSource() {
        return this.src;
    }

    public DynamicXsltOptions setSource(@Nullable Source src) {
        this.src = src;

        return this;
    }
}
