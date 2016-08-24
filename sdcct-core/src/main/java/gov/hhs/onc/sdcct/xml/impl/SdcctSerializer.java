package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.config.utils.SdcctOptionUtils;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.transform.impl.SdcctPipelineConfiguration;
import gov.hhs.onc.sdcct.transform.impl.SdcctProcessor;
import gov.hhs.onc.sdcct.transform.impl.SdcctPullEventSource;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.FilterFactory;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.Sender;
import net.sf.saxon.expr.parser.ExplicitLocation;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.SchemaType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctSerializer extends Serializer {
    private static class PrettyXmlReceiver extends ProxyReceiver {
        private static class PrettyXmlElement {
            private boolean childElems;

            public boolean getChildElements() {
                return this.childElems;
            }

            public void setChildElements(boolean childElems) {
                this.childElems = childElems;
            }
        }

        private boolean omitDecl;
        private int indentLen;
        private String indentStr;
        private int depth;
        private Deque<PrettyXmlElement> elems = new LinkedList<>();
        private boolean decl;

        public PrettyXmlReceiver(Receiver nextReceiver, boolean omitDecl, @Nonnegative int indentLen) {
            super(nextReceiver);

            this.omitDecl = omitDecl;

            char[] indentChars = new char[(this.indentLen = indentLen)];
            Arrays.fill(indentChars, SdcctStringUtils.SPACE_CHAR);

            this.indentStr = new String(indentChars, 0, this.indentLen);
        }

        @Override
        public void characters(CharSequence chars, Location loc, int props) throws XPathException {
            String str = chars.toString().trim();

            if (!str.isEmpty()) {
                super.characters(str, loc, props);
            }
        }

        @Override
        public void endElement() throws XPathException {
            PrettyXmlElement elem = this.elems.pop();

            this.depth--;

            if (elem.getChildElements()) {
                this.writeNewline();
                this.writeIndent();
            }

            super.endElement();
        }

        @Override
        public void startElement(NodeName elemName, SchemaType schemaType, Location loc, int props) throws XPathException {
            boolean elemAvailable = !this.elems.isEmpty();

            if (elemAvailable) {
                this.elems.peek().setChildElements(true);
            }

            if (elemAvailable || this.decl) {
                this.writeNewline();
            }

            this.writeIndent();

            this.depth++;

            super.startElement(elemName, schemaType, loc, props);

            this.elems.push(new PrettyXmlElement());
        }

        @Override
        public void processingInstruction(String name, CharSequence value, Location loc, int props) throws XPathException {
            super.processingInstruction(name, value, loc, props);

            this.writeNewline();
        }

        @Override
        public void startDocument(int props) throws XPathException {
            super.startDocument(props);

            this.decl = !this.omitDecl;
        }

        private void writeNewline() throws XPathException {
            super.characters(StringUtils.LF, ExplicitLocation.UNKNOWN_LOCATION, 0);
        }

        private void writeIndent() throws XPathException {
            for (int a = 0; a < this.depth; a++) {
                super.characters(this.indentStr, ExplicitLocation.UNKNOWN_LOCATION, 0);
            }
        }
    }

    private final static String INDENT_OUT_PROP_KEY = Property.INDENT.getQName().getClarkName();
    private final static String INDENT_SPACES_OUT_PROP_KEY = Property.SAXON_INDENT_SPACES.getQName().getClarkName();
    private final static String OMIT_XML_DECL_OUT_PROP_KEY = Property.OMIT_XML_DECLARATION.getQName().getClarkName();

    @Autowired
    private SdcctXmlOutputFactory xmlOutFactory;

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    public SdcctSerializer(SdcctProcessor processor) {
        super(processor);

        this.setDefaultOutputProperties(processor.getUnderlyingConfiguration().getDefaultSerializationProperties());
    }

    public byte[] serialize(Source src, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        return this.serialize(src, new ByteArrayResult(), parseOpts, outProps).getBytes();
    }

    public <T extends Result> T serialize(Source src, T result, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        SdcctConfiguration config = this.getProcessor().getUnderlyingConfiguration();
        SdcctPipelineConfiguration pipelineConfig = config.makePipelineConfiguration();

        if (src instanceof StreamSource) {
            try {
                src = new SdcctPullEventSource(SdcctTransformUtils.getPublicId(src), src.getSystemId(),
                    new XmlStreamReaderEventIterator(this.xmlInFactory.createXMLStreamReader(src), pipelineConfig));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        Receiver receiver = this.getReceiver(config, pipelineConfig, result, parseOpts, outProps);

        try {
            Sender.send(src, receiver, null);

            receiver.close();

            return result;
        } catch (XPathException e) {
            throw new SaxonApiException(String.format("Unable to serialize source (class=%s, sysId=%s) to result (class=%s, sysId=%s).",
                src.getClass().getName(), src.getSystemId(), result.getClass().getName(), result.getSystemId()), e);
        }
    }

    @Override
    public ReceiverXmlStreamWriter getXMLStreamWriter() throws SaxonApiException {
        SdcctConfiguration config = this.getProcessor().getUnderlyingConfiguration();

        return this.getXMLStreamWriter(config, config.makePipelineConfiguration(), this.getResult(), null, null);
    }

    public ReceiverXmlStreamWriter getXMLStreamWriter(SdcctConfiguration config, SdcctPipelineConfiguration pipelineConfig, Result result,
        @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        return new ReceiverXmlStreamWriter(this.getReceiver(config, pipelineConfig, result, parseOpts, outProps));
    }

    @Override
    public Receiver getReceiver(Configuration config) throws SaxonApiException {
        return this.getReceiver(config.makePipelineConfiguration());
    }

    @Override
    public Receiver getReceiver(PipelineConfiguration pipelineConfig) throws SaxonApiException {
        return this.getReceiver(((SdcctConfiguration) pipelineConfig.getConfiguration()), ((SdcctPipelineConfiguration) pipelineConfig), this.getResult(), null,
            null);
    }

    public Receiver getReceiver(SdcctConfiguration config, SdcctPipelineConfiguration pipelineConfig, Result result, @Nullable ParseOptions parseOpts,
        @Nullable Properties outProps) throws SaxonApiException {
        boolean pretty = SdcctOptionUtils.getBooleanValue((outProps = this.mergeOutputProperties(outProps)), INDENT_OUT_PROP_KEY, false);
        int indentLen = SdcctOptionUtils.getIntegerValue(outProps, INDENT_SPACES_OUT_PROP_KEY, -1);

        outProps.setProperty(INDENT_OUT_PROP_KEY, SdcctOptionUtils.NO_VALUE);

        if (result instanceof StreamResult) {
            try {
                result = new StAXResult(this.xmlOutFactory.createXMLStreamWriter(result));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        Receiver receiver;

        try {
            receiver = config.getSerializerFactory().getReceiver(result, pipelineConfig, outProps);
        } catch (XPathException e) {
            throw new SaxonApiException(
                String.format("Unable to build Saxon serializer receiver for result (class=%s, sysId=%s).", result.getClass().getName(), result.getSystemId()),
                e);
        }

        if (receiver.getSystemId() == null) {
            receiver.setSystemId(result.getSystemId());
        }

        if (pretty) {
            (receiver = new PrettyXmlReceiver(receiver, SdcctOptionUtils.getBooleanValue(outProps, OMIT_XML_DECL_OUT_PROP_KEY, false), indentLen))
                .setPipelineConfiguration(pipelineConfig);
        }

        ParseOptions pipelineParseOpts = pipelineConfig.getParseOptions();

        if (parseOpts != null) {
            pipelineParseOpts.merge(parseOpts);
        }

        List<FilterFactory> parseFilters = pipelineParseOpts.getFilters();

        if (!CollectionUtils.isEmpty(parseFilters)) {
            for (int a = (parseFilters.size() - 1); a >= 0; a--) {
                receiver = parseFilters.get(a).makeFilter(receiver);
            }

            parseFilters.clear();
        }

        return receiver;
    }

    private Properties mergeOutputProperties(@Nullable Properties outProps) {
        Properties mergedOutProps = super.getCombinedOutputProperties();

        if (outProps != null) {
            mergedOutProps.putAll(SdcctTransformUtils.buildOutputProperties(outProps));
        }

        return mergedOutProps;
    }

    @Override
    public Properties getLocalOutputProperties() {
        return super.getLocalOutputProperties();
    }

    public void setLocalOutputProperties(Properties localOutProps) {
        SdcctTransformUtils.buildOutputProperties(localOutProps).forEach(
            (localOutPropKey, localOutPropValue) -> this.setOutputProperty(new QName(((StructuredQName) localOutPropKey)), ((String) localOutPropValue)));
    }

    @Override
    public SdcctProcessor getProcessor() {
        return ((SdcctProcessor) super.getProcessor());
    }
}
