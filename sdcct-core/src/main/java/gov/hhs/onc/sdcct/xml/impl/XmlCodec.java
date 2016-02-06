package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.sax.WstxSAXParser;
import gov.hhs.onc.sdcct.io.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.io.impl.ByteArraySource;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodec;
import gov.hhs.onc.sdcct.transform.content.impl.ContentDecodeOptions;
import gov.hhs.onc.sdcct.transform.content.impl.ContentEncodeOptions;
import gov.hhs.onc.sdcct.transform.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.transform.impl.SdcctSerializer;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.jaxb.impl.JaxbResult;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import org.springframework.beans.factory.annotation.Autowired;

public class XmlCodec extends AbstractContentCodec {
    @Autowired
    private JaxbContextRepository jaxbContextRepo;

    @Autowired
    private SdcctSaxParserFactory saxParserFactory;

    @Autowired
    private SdcctDocumentBuilder docBuilder;

    @Resource(name = "serializerXml")
    private SdcctSerializer serializer;

    @Resource(name = "serializerXmlPretty")
    private SdcctSerializer prettySerializer;

    public XmlCodec() {
        super(SdcctContentType.XML);
    }

    @Override
    public byte[] encode(Object src, @Nullable ContentEncodeOptions opts) throws Exception {
        return this.encode(src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T encode(Object src, T result, @Nullable ContentEncodeOptions opts) throws Exception {
        return this.encode(this.jaxbContextRepo.buildSource(src, null), result, opts);
    }

    public byte[] encode(Source src, @Nullable ContentEncodeOptions opts) throws Exception {
        return this.encode(src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T encode(Source src, T result, @Nullable ContentEncodeOptions opts) throws Exception {
        // noinspection ConstantConditions
        return ((opts = this.defaultEncodeOpts.clone().merge(opts)).getOption(ContentEncodeOptions.PRETTY) ? this.prettySerializer : this.serializer)
            .serialize(src, result, opts.getParseOptions(), opts.getOutputProperties());
    }

    @Override
    public <T> T decode(byte[] src, Class<T> resultClass, @Nullable ContentDecodeOptions opts) throws Exception {
        return this.decode(new ByteArraySource(src), resultClass, opts);
    }

    public <T> T decode(Source src, Class<T> resultClass, @Nullable ContentDecodeOptions opts) throws Exception {
        JaxbResult<T> result = this.jaxbContextRepo.buildResult(resultClass, null);

        WstxSAXParser saxParser = this.saxParserFactory.newSAXParser();
        saxParser.setContentHandler(result.getHandler());
        saxParser.parse(SAXSource.sourceToInputSource(src));

        return result.getResult();
    }

    public XdmDocument decode(byte[] src, @Nullable ContentDecodeOptions opts) throws Exception {
        return this.decode(new ByteArraySource(src), opts);
    }

    public XdmDocument decode(Source src, @Nullable ContentDecodeOptions opts) throws Exception {
        return this.docBuilder.build(src);
    }
}
