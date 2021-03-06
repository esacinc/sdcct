package gov.hhs.onc.sdcct.xml.html.impl;

import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctPullEventSource;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import gov.hhs.onc.sdcct.xml.html.HtmlTranscodeOptions;
import gov.hhs.onc.sdcct.xml.saxon.impl.AbstractSdcctCdataXmlFilter;
import gov.hhs.onc.sdcct.xml.saxon.impl.AugmentedDestination;
import gov.hhs.onc.sdcct.xml.saxon.impl.ReceiverDestination;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctSerializer;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory;
import gov.hhs.onc.sdcct.xml.xslt.DynamicXsltOptions;
import gov.hhs.onc.sdcct.xml.xslt.impl.DynamicXsltOptionsImpl;
import gov.hhs.onc.sdcct.xml.xslt.saxon.impl.SdcctXsltExecutable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.evpull.StaxToEventBridge;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.type.SchemaType;
import org.springframework.beans.factory.annotation.Autowired;

public class HtmlTranscoder {
    private static class HtmlCdataXmlFilter extends AbstractSdcctCdataXmlFilter {
        private final static Set<String> HTML_CDATA_ELEM_LOCAL_NAMES = Stream.of("script", "style").collect(Collectors.toSet());

        public HtmlCdataXmlFilter(Receiver nextReceiver) {
            super(nextReceiver);
        }

        @Override
        protected boolean isCdataElement(NodeName elemName, SchemaType type, Location loc, int props) {
            return (elemName.getURI().equals(XMLConstants.NULL_NS_URI) && HTML_CDATA_ELEM_LOCAL_NAMES.contains(elemName.getLocalPart()));
        }
    }

    @Resource(name = "serializerHtml")
    private SdcctSerializer serializer;

    @Resource(name = "serializerHtmlPretty")
    private SdcctSerializer prettySerializer;

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    @Autowired
    private SdcctSaxonConfiguration config;

    private HtmlTranscodeOptions defaultTranscodeOpts;

    public HtmlTranscoder(HtmlTranscodeOptions defaultTranscodeOpts) {
        this.defaultTranscodeOpts = defaultTranscodeOpts;
    }

    public byte[] transcode(SdcctXsltExecutable xsltExec, Source src, @Nullable HtmlTranscodeOptions opts) throws Exception {
        return this.transcode(xsltExec, src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T transcode(SdcctXsltExecutable xsltExec, Source src, T result, @Nullable HtmlTranscodeOptions opts) throws Exception {
        if (src instanceof StreamSource) {
            StaxToEventBridge srcEventIterator = new StaxToEventBridge();
            srcEventIterator.setPipelineConfiguration(this.config.makePipelineConfiguration());
            srcEventIterator.setXMLStreamReader(this.xmlInFactory.createXMLStreamReader(src));

            src = new SdcctPullEventSource(SdcctTransformUtils.getPublicId(src), src.getSystemId(), srcEventIterator);
        }

        // noinspection ConstantConditions
        Receiver receiver =
            ((opts = this.defaultTranscodeOpts.clone().merge(opts)).getOption(ContentCodecOptions.PRETTY) ? this.prettySerializer : this.serializer)
                .getReceiver(this.config, this.config.makePipelineConfiguration(), result, null, null);

        DynamicXsltOptions xsltOpts = new DynamicXsltOptionsImpl();
        xsltOpts.setSource(src);
        xsltOpts.setDestination(new AugmentedDestination(new ReceiverDestination<>(receiver)).addFilters(HtmlCdataXmlFilter::new));
        xsltOpts.setVariables(opts.getVariables());

        xsltExec.load(xsltOpts).transform();

        return result;
    }

    public HtmlTranscodeOptions getDefaultTranscodeOptions() {
        return this.defaultTranscodeOpts;
    }
}
