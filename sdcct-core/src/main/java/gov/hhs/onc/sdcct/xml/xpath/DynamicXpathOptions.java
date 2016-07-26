package gov.hhs.onc.sdcct.xml.xpath;

import gov.hhs.onc.sdcct.xml.DynamicXmlTransformOptions;
import javax.annotation.Nullable;
import net.sf.saxon.om.Item;

public interface DynamicXpathOptions extends DynamicXmlTransformOptions<DynamicXpathOptions> {
    public boolean hasContextItem();

    @Nullable
    public Item getContextItem();

    public DynamicXpathOptions setContextItem(@Nullable Item contextItem);
}
