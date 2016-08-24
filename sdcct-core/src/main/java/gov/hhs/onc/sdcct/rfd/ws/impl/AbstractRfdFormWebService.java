package gov.hhs.onc.sdcct.rfd.ws.impl;

import gov.hhs.onc.sdcct.form.ws.impl.AbstractFormWebService;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.search.RfdSearchService;
import gov.hhs.onc.sdcct.rfd.ws.RfdFormWebService;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdInteractionWsMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdResourceWsMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdWsMetadata;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.xml.html.impl.HtmlTranscoder;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRfdFormWebService
    extends AbstractFormWebService<RfdResource, RfdResourceRegistry<?>, RfdSearchService<?>, RfdInteractionWsMetadata, RfdResourceWsMetadata<?>, RfdWsMetadata>
    implements RfdFormWebService {
    @Autowired
    protected HtmlTranscoder htmlTranscoder;

    @Autowired
    protected XmlCodec xmlCodec;

    @Autowired
    protected SdcctConfiguration config;

    @Resource
    protected WebServiceContext wsContext;
}
