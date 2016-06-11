package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.config.utils.SdcctOptionUtils;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.transform.impl.SdcctPipelineConfiguration;
import gov.hhs.onc.sdcct.transform.impl.SdcctProcessor;
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
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.FilterFactory;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.Sender;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.stax2.ri.Stax2WriterAdapter;
import org.codehaus.stax2.util.StreamWriter2Delegate;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctSerializer extends Serializer {
    private static class PrettyXmlStreamWriter extends StreamWriter2Delegate {
        private static class PrettyXmlElement {
            private String nsPrefix;
            private String nsUri;
            private boolean childElems;

            public PrettyXmlElement(@Nullable String nsPrefix, @Nullable String nsUri) {
                this.nsPrefix = nsPrefix;
                this.nsUri = nsUri;
            }

            public boolean getChildElements() {
                return this.childElems;
            }

            public void setChildElements(boolean childElems) {
                this.childElems = childElems;
            }

            public boolean hasNamespacePrefix() {
                return (this.nsPrefix != null);
            }

            @Nullable
            public String getNamespacePrefix() {
                return this.nsPrefix;
            }

            public boolean hasNamespaceUri() {
                return (this.nsUri != null);
            }

            @Nullable
            public String getNamespaceUri() {
                return this.nsUri;
            }
        }

        private final static char[] NEWLINE_CHARS = new char[] { SdcctStringUtils.LF_CHAR };

        private int numIndentSpaces;
        private char[] indentSpaces;
        private int indentLvl;
        private Deque<PrettyXmlElement> elems = new LinkedList<>();
        private boolean decl;

        public PrettyXmlStreamWriter(XMLStreamWriter delegate, @Nonnegative int numIndentSpaces) {
            super(Stax2WriterAdapter.wrapIfNecessary(delegate));

            this.setParent(this.getParent());

            Arrays.fill((this.indentSpaces = new char[(this.numIndentSpaces = numIndentSpaces)]), SdcctStringUtils.SPACE_CHAR);
        }

        @Override
        public void writeEndElement() throws XMLStreamException {
            PrettyXmlElement elem = this.elems.pop();

            this.indentLvl--;

            if (elem.getChildElements()) {
                this.writeNewline();
                this.writeIndent();
            }

            this.mDelegate2.writeEndElement();
        }

        @Override
        public void writeStartElement(String elemLocalName) throws XMLStreamException {
            this.writeStartElement(null, elemLocalName);
        }

        @Override
        public void writeStartElement(String elemNsUri, String elemLocalName) throws XMLStreamException {
            this.writeStartElement(null, elemNsUri, elemLocalName);
        }

        @Override
        public void writeStartElement(String elemNsPrefix, String elemNsUri, String elemLocalName) throws XMLStreamException {
            boolean elemsAvailable = !this.elems.isEmpty();

            if (this.decl || elemsAvailable) {
                this.writeNewline();
            }

            this.writeIndent();

            this.indentLvl++;

            if (elemsAvailable) {
                this.elems.peek().setChildElements(true);
            }

            if (elemNsPrefix == null) {
                if (elemNsUri != null) {
                    this.mDelegate2.writeStartElement(elemNsUri, elemLocalName);
                } else {
                    this.mDelegate2.writeStartElement(elemLocalName);
                }
            } else {
                this.mDelegate2.writeStartElement(elemNsPrefix, elemNsUri, elemLocalName);
            }

            this.elems.push(new PrettyXmlElement(elemNsPrefix, elemNsUri));
        }

        @Override
        public void writeStartDocument(String declVersion, String declEnc, boolean declStandalone) throws XMLStreamException {
            this.decl = true;

            super.writeStartDocument(declVersion, declEnc, declStandalone);
        }

        @Override
        public void writeStartDocument(String declEnc, String declVersion) throws XMLStreamException {
            this.decl = true;

            super.writeStartDocument(declEnc, declVersion);
        }

        @Override
        public void writeStartDocument(String declVersion) throws XMLStreamException {
            this.decl = true;

            super.writeStartDocument(declVersion);
        }

        @Override
        public void writeStartDocument() throws XMLStreamException {
            this.decl = true;

            super.writeStartDocument();
        }

        private void writeNewline() throws XMLStreamException {
            this.mDelegate2.writeCharacters(NEWLINE_CHARS, 0, 1);
        }

        private void writeIndent() throws XMLStreamException {
            for (int a = 0; a < this.indentLvl; a++) {
                this.mDelegate2.writeCharacters(this.indentSpaces, 0, this.numIndentSpaces);
            }
        }
    }

    private final static String INDENT_OUT_PROP_KEY = Property.INDENT.getQName().getClarkName();
    private final static String INDENT_SPACES_OUT_PROP_KEY = Property.SAXON_INDENT_SPACES.getQName().getClarkName();

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
        if (src instanceof StreamSource) {
            try {
                src = new StAXSource(this.xmlInFactory.createXMLStreamReader(src));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        SdcctConfiguration config = this.getProcessor().getUnderlyingConfiguration();
        Receiver receiver = this.getReceiver(config, config.makePipelineConfiguration(), result, parseOpts, outProps);

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
        boolean indent = SdcctOptionUtils.getBooleanValue((outProps = this.mergeOutputProperties(outProps)), INDENT_OUT_PROP_KEY, false);
        int indentSpaces = SdcctOptionUtils.getIntegerValue(outProps, INDENT_SPACES_OUT_PROP_KEY, -1);

        outProps.setProperty(INDENT_OUT_PROP_KEY, SdcctOptionUtils.NO_VALUE);

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

        if (indent) {
            (receiver = new ReceiverToXMLStreamWriter(new PrettyXmlStreamWriter(new ReceiverXmlStreamWriter(receiver), indentSpaces)))
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
