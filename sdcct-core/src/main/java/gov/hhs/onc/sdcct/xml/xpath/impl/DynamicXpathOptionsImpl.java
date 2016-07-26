package gov.hhs.onc.sdcct.xml.xpath.impl;

import gov.hhs.onc.sdcct.xml.impl.AbstractDynamicXmlTransformOptions;
import gov.hhs.onc.sdcct.xml.xpath.DynamicXpathOptions;
import javax.annotation.Nullable;
import net.sf.saxon.om.Item;

public class DynamicXpathOptionsImpl extends AbstractDynamicXmlTransformOptions<DynamicXpathOptions> implements DynamicXpathOptions {
    private final static long serialVersionUID = 0L;

    private Item contextItem;

    public DynamicXpathOptionsImpl() {
        super();
    }

    @Override
    protected void mergeInternal(DynamicXpathOptions mergeOpts) {
        super.mergeInternal(mergeOpts);

        this.contextData.putAll(mergeOpts.getContextData());

        if (mergeOpts.hasContextItem()) {
            this.contextItem = mergeOpts.getContextItem();
        }
    }

    @Override
    protected DynamicXpathOptions cloneInternal() {
        return new DynamicXpathOptionsImpl();
    }

    @Override
    public boolean hasContextItem() {
        return (this.contextItem != null);
    }

    @Nullable
    @Override
    public Item getContextItem() {
        return this.contextItem;
    }

    @Override
    public DynamicXpathOptions setContextItem(@Nullable Item contextItem) {
        this.contextItem = contextItem;

        return this;
    }
}
