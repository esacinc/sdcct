package gov.hhs.onc.sdcct.transform.saxon.impl;

import com.ctc.wstx.sax.WstxSAXParser;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import gov.hhs.onc.sdcct.xml.XmlStreamAccessor;
import gov.hhs.onc.sdcct.xml.impl.SdcctSaxParserFactory;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stax.StAXSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.Builder;
import net.sf.saxon.event.NamespaceReducer;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Sender;
import net.sf.saxon.event.SequenceReceiver;
import net.sf.saxon.evpull.EventIterator;
import net.sf.saxon.evpull.PullEventSource;
import net.sf.saxon.evpull.StaxToEventBridge;
import net.sf.saxon.lib.AugmentedSource;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.om.TreeInfo;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.SchemaType;
import net.sf.saxon.type.SimpleType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.ErrorHandler;

public class SdcctSaxonConfiguration extends Configuration implements InitializingBean {
    private static class XmlStreamBuilderFilter extends ProxyReceiver {
        private XmlStreamAccessor accessor;

        public XmlStreamBuilderFilter(XmlStreamAccessor accessor, SequenceReceiver nextReceiver) {
            super(nextReceiver);

            this.accessor = accessor;
        }

        @Override
        public void characters(CharSequence chars, net.sf.saxon.expr.parser.Location loc, int props) throws XPathException {
            super.characters(chars, this.buildLocation(), props);
        }

        @Override
        public void attribute(NodeName nameCode, SimpleType type, CharSequence value, net.sf.saxon.expr.parser.Location loc, int props) throws XPathException {
            super.attribute(nameCode, type, value, this.buildLocation(), props);
        }

        @Override
        public void startElement(NodeName elemName, SchemaType type, net.sf.saxon.expr.parser.Location loc, int props) throws XPathException {
            super.startElement(elemName, type, this.buildLocation(), props);
        }

        @Override
        public void processingInstruction(String target, CharSequence data, net.sf.saxon.expr.parser.Location loc, int props) throws XPathException {
            super.processingInstruction(target, data, this.buildLocation(), props);
        }

        @Override
        public void comment(CharSequence chars, net.sf.saxon.expr.parser.Location loc, int props) throws XPathException {
            super.comment(chars, this.buildLocation(), props);
        }

        @Override
        public void append(Item item, net.sf.saxon.expr.parser.Location loc, int copyNamespaces) throws XPathException {
            super.append(item, this.buildLocation(), copyNamespaces);
        }

        private SdcctLocation buildLocation() {
            Location loc = this.accessor.getLocation();

            return ((loc instanceof SdcctLocation) ? ((SdcctLocation) loc) : new SdcctLocation(loc));
        }
    }

    @Autowired
    private SdcctSaxParserFactory saxParserFactory;

    @Override
    public TreeInfo buildDocumentTree(Source src, ParseOptions parseOpts) throws XPathException {
        parseOpts = new ParseOptions(parseOpts);

        if ((src = this.resolveSource(src, this)) instanceof AugmentedSource) {
            parseOpts.merge(((AugmentedSource) src).getParseOptions());
        }

        parseOpts.applyDefaults(this);

        boolean closeSrc = parseOpts.isPleaseCloseAfterUse();

        try {
            Builder builder = parseOpts.getModel().makeBuilder(this.makePipelineConfiguration());
            SequenceReceiver receiver = new NamespaceReducer(builder);

            if (src instanceof PullEventSource) {
                EventIterator srcEventIterator = ((PullEventSource) src).getEventIterator();

                if (srcEventIterator instanceof StaxToEventBridge) {
                    receiver = new XmlStreamBuilderFilter(((XmlStreamAccessor) ((StaxToEventBridge) srcEventIterator).getXMLStreamReader()), receiver);
                }
            }

            Sender.send(src, receiver, parseOpts);

            NodeInfo docElemInfo = builder.getCurrentRoot();

            builder.reset();

            return docElemInfo.getTreeInfo();
        } finally {
            if (closeSrc) {
                ParseOptions.close(src);
            }
        }
    }

    @Override
    public SdcctPipelineConfiguration makePipelineConfiguration() {
        return new SdcctPipelineConfiguration(this);
    }

    @Override
    public Source resolveSource(Source src, Configuration config) throws XPathException {
        return ((src instanceof StAXSource) ? src : super.resolveSource(src, config));
    }

    @Override
    public WstxSAXParser makeParser(@Nullable String className) throws TransformerFactoryConfigurationError {
        return this.saxParserFactory.newSAXParser();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setSourceParserClass(StringUtils.EMPTY);
        this.setStyleParserClass(StringUtils.EMPTY);

        this.getParseOptions().setXMLReaderMaker(this.saxParserFactory::newSAXParser);
    }

    public void setConfigurationProperties(Map<String, ?> configProps) {
        configProps.forEach(this::setConfigurationProperty);
    }

    @Override
    public void setDefaultSerializationProperties(Properties defaultOutProps) {
        super.setDefaultSerializationProperties(SdcctTransformUtils.buildOutputProperties(defaultOutProps));
    }

    public ErrorHandler getErrorHandler() {
        return this.getParseOptions().getErrorHandler();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.getParseOptions().setErrorHandler(errorHandler);
    }

    public void setXsltVersion(String xsltVersion) {
        this.setConfigurationProperty(FeatureKeys.XSLT_VERSION, xsltVersion);
    }
}
