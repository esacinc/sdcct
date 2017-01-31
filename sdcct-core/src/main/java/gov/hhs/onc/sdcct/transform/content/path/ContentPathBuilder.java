package gov.hhs.onc.sdcct.transform.content.path;

import gov.hhs.onc.sdcct.transform.SdcctTransformException;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import java.util.LinkedList;
import javax.annotation.Nullable;
import net.sf.saxon.om.NodeInfo;
import org.springframework.beans.factory.BeanClassLoaderAware;

public interface ContentPathBuilder extends BeanClassLoaderAware {
    public ContentPath build(@Nullable JaxbContextRepository jaxbContextRepo, boolean typed, NodeInfo nodeInfo) throws SdcctTransformException;

    public ContentPath build(@Nullable JaxbContextRepository jaxbContextRepo, boolean typed, LinkedList<ContentPathSegment<?, ?>> segments)
        throws SdcctTransformException;
}
