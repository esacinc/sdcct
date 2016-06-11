package gov.hhs.onc.sdcct.rfd.parameter.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.data.parameter.ResourceParam;
import gov.hhs.onc.sdcct.data.parameter.impl.AbstractResourceParamProcessor;
import gov.hhs.onc.sdcct.data.parameter.impl.UriResourceParamImpl;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.impl.RfdResourceImpl;
import gov.hhs.onc.sdcct.rfd.parameter.RfdResourceParamProcessor;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import gov.hhs.onc.sdcct.sdc.impl.AbstractIdentifiedExtensionType;
import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import net.sf.saxon.s9api.XdmItem;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.springframework.stereotype.Component;

@Component("resourceParamProcRfd")
public class RfdResourceParamProcessorImpl extends AbstractResourceParamProcessor<IdentifiedExtensionType, RfdResourceMetadata<?>, RfdResource>
    implements RfdResourceParamProcessor {
    public RfdResourceParamProcessorImpl() {
        super(SpecificationType.RFD, IdentifiedExtensionType.class, AbstractIdentifiedExtensionType.class, RfdResource.class, RfdResourceImpl.class);
    }

    @Override
    protected void buildUriResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name, ResourceParamMetadata metadata,
        XdmItem item, RfdResource entity) throws Exception {
        String value = item.getStringValue();

        resourceParams.put(new MultiKey<>(name, value), new UriResourceParamImpl(entity, false, name, URI.create(value)));
    }
}
