package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodec;
import gov.hhs.onc.sdcct.transform.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.xml.XmlDecodeOptions;
import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.codehaus.stax2.XMLStreamWriter2;
import org.codehaus.stax2.ri.Stax2WriterAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public class XmlCodec extends AbstractContentCodec<XmlDecodeOptions, XmlEncodeOptions> {
    @Autowired
    private JaxbContextRepository jaxbContextRepo;

    @Resource(name = "serializerXml")
    private SdcctSerializer serializer;

    @Resource(name = "serializerXmlPretty")
    private SdcctSerializer prettySerializer;

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    @Autowired
    private SdcctConfiguration config;

    public XmlCodec(XmlDecodeOptions defaultDecodeOpts, XmlEncodeOptions defaultEncodeOpts) {
        super(SdcctContentType.XML, defaultDecodeOpts, defaultEncodeOpts);
    }

    @Override
    public byte[] encode(Object src, @Nullable XmlEncodeOptions opts) throws Exception {
        return this.encode(src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T encode(Object src, T result, @Nullable XmlEncodeOptions opts) throws Exception {
        // noinspection ConstantConditions
        XMLStreamWriter2 resultWriter = Stax2WriterAdapter.wrapIfNecessary(
            ((opts = this.defaultEncodeOpts.clone().merge(opts)).getOption(ContentCodecOptions.PRETTY) ? this.prettySerializer : this.serializer)
                .getXMLStreamWriter(this.config, this.config.makePipelineConfiguration(), result, opts.getParseOptions(), opts.getOutputProperties()));
        JaxbTypeMetadata<?, ?> srcTypeMetadata = this.jaxbContextRepo.findTypeMetadata(src.getClass());

        // noinspection ConstantConditions
        if (opts.getOption(ContentCodecOptions.VALIDATE)) {
            // TODO: validate
        }

        this.jaxbContextRepo.buildMarshaller(src, null).marshal(this.jaxbContextRepo.buildSource(srcTypeMetadata, src), resultWriter);

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
        // noinspection ConstantConditions
        if ((opts = this.defaultDecodeOpts.clone().merge(opts)).getOption(ContentCodecOptions.VALIDATE)) {
            // TODO: validate
        }

        return this.jaxbContextRepo.buildUnmarshaller(resultClass, null).unmarshal(this.xmlInFactory.createXMLStreamReader(src), resultClass).getValue();
    }
}
