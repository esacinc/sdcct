package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodec;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctPipelineConfiguration;
import gov.hhs.onc.sdcct.xml.XmlDecodeOptions;
import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctSerializer;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import net.sf.saxon.evpull.Decomposer;
import net.sf.saxon.evpull.EventToStaxBridge;
import net.sf.saxon.om.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class XmlCodec extends AbstractContentCodec<XmlDecodeOptions, XmlEncodeOptions> {
    /**
     * Workaround for the fact that the {@link com.sun.xml.bind.v2.runtime.unmarshaller.StAXStreamConnector#bridge JAXB Unmarshaller StAX bridging} calls
     * {@link XMLStreamReader#next()} an extra time.
     */
    public static class UnmarshallingXmlStreamReader extends EventToStaxBridge {
        public UnmarshallingXmlStreamReader(NodeInfo srcNodeInfo, SdcctPipelineConfiguration pipelineConfig) {
            super(new Decomposer(srcNodeInfo, pipelineConfig), pipelineConfig);
        }

        @Override
        public int next() throws XMLStreamException {
            try {
                return super.next();
            } catch (NoSuchElementException ignored) {
                return -1;
            }
        }
    }

    @Autowired
    private JaxbContextRepository jaxbContextRepo;

    @Resource(name = "serializerXml")
    private SdcctSerializer serializer;

    @Resource(name = "serializerXmlPretty")
    private SdcctSerializer prettySerializer;

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    @Autowired
    private SdcctSaxonConfiguration config;

    public XmlCodec(XmlDecodeOptions defaultDecodeOpts, XmlEncodeOptions defaultEncodeOpts) {
        super(SdcctContentType.XML, defaultDecodeOpts, defaultEncodeOpts);
    }

    @Override
    public byte[] encode(Object src, @Nullable XmlEncodeOptions opts) throws Exception {
        return this.encode(src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T encode(Object src, T result, @Nullable XmlEncodeOptions opts) throws Exception {
        // noinspection ConstantConditions
        XMLStreamWriter resultWriter =
            ((opts = this.defaultEncodeOpts.clone().merge(opts)).getOption(ContentCodecOptions.PRETTY) ? this.prettySerializer : this.serializer)
                .getXMLStreamWriter(this.config, this.config.makePipelineConfiguration(), result, opts.getParseOptions(), opts.getOutputProperties());

        this.jaxbContextRepo.buildMarshaller(src, null).marshal(this.jaxbContextRepo.buildSource(this.jaxbContextRepo.findTypeMetadata(src.getClass()), src),
            resultWriter);

        resultWriter.flush();
        resultWriter.close();

        return result;
    }

    public byte[] encode(Source src, @Nullable XmlEncodeOptions opts) throws Exception {
        return this.encode(src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T encode(Source src, T result, @Nullable XmlEncodeOptions opts) throws Exception {
        // noinspection ConstantConditions
        return ((opts = this.defaultEncodeOpts.clone().merge(opts)).getOption(ContentCodecOptions.PRETTY) ? this.prettySerializer : this.serializer)
            .serialize(src, result, opts.getParseOptions(), opts.getOutputProperties());
    }

    @Override
    public <T> T decode(byte[] src, Class<T> resultClass, @Nullable XmlDecodeOptions opts) throws Exception {
        return this.decode(new ByteArraySource(src), resultClass, opts);
    }

    public <T> T decode(Source src, Class<T> resultClass, @Nullable XmlDecodeOptions opts) throws Exception {
        return this.jaxbContextRepo.buildUnmarshaller(resultClass, null)
            .unmarshal(((src instanceof NodeInfo)
                ? new UnmarshallingXmlStreamReader(((NodeInfo) src), this.config.makePipelineConfiguration()) : this.xmlInFactory.createXMLStreamReader(src)),
                resultClass)
            .getValue();
    }
}
