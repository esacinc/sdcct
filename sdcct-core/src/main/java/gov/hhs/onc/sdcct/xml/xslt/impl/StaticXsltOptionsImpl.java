package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.xml.impl.AbstractStaticXmlTransformOptions;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;

public class StaticXsltOptionsImpl extends AbstractStaticXmlTransformOptions<StaticXsltOptions> implements StaticXsltOptions {
    private final static long serialVersionUID = 0L;

    public StaticXsltOptionsImpl() {
        super();
    }

    @Override
    protected StaticXsltOptions cloneInternal() {
        return new StaticXsltOptionsImpl();
    }
}
