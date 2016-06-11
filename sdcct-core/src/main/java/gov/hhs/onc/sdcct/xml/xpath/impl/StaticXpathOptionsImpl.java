package gov.hhs.onc.sdcct.xml.xpath.impl;

import gov.hhs.onc.sdcct.xml.impl.AbstractStaticXmlTransformOptions;
import gov.hhs.onc.sdcct.xml.xpath.StaticXpathOptions;

public class StaticXpathOptionsImpl extends AbstractStaticXmlTransformOptions<StaticXpathOptions> implements StaticXpathOptions {
    private final static long serialVersionUID = 0L;

    public StaticXpathOptionsImpl() {
        super();
    }

    @Override
    protected StaticXpathOptions cloneInternal() {
        return new StaticXpathOptionsImpl();
    }
}
