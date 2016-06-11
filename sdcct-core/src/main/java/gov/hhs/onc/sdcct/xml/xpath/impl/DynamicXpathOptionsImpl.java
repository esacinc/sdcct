package gov.hhs.onc.sdcct.xml.xpath.impl;

import gov.hhs.onc.sdcct.xml.impl.AbstractDynamicXmlTransformOptions;
import gov.hhs.onc.sdcct.xml.xpath.DynamicXpathOptions;

public class DynamicXpathOptionsImpl extends AbstractDynamicXmlTransformOptions<DynamicXpathOptions> implements DynamicXpathOptions {
    private final static long serialVersionUID = 0L;

    public DynamicXpathOptionsImpl() {
        super();
    }

    @Override
    protected DynamicXpathOptions cloneInternal() {
        return new DynamicXpathOptionsImpl();
    }
}
