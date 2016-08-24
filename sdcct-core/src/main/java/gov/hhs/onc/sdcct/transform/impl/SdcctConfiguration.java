package gov.hhs.onc.sdcct.transform.impl;

import com.ctc.wstx.sax.WstxSAXParser;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import gov.hhs.onc.sdcct.xml.impl.SdcctSaxParserFactory;
import gov.hhs.onc.sdcct.xml.impl.SdcctSequenceNormalizer;
import gov.hhs.onc.sdcct.xml.impl.XmlStreamWriterReceiver;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.ComplexContentOutputter;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.SequenceReceiver;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.SerializerFactory;
import net.sf.saxon.serialize.CharacterMapIndex;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.stax2.ri.Stax2WriterAdapter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.ErrorHandler;

public class SdcctConfiguration extends Configuration implements InitializingBean {
    private static class SdcctSerializerFactory extends SerializerFactory {
        public SdcctSerializerFactory(SdcctConfiguration config) {
            super(config);
        }

        @Override
        public SequenceReceiver getReceiver(Result result, PipelineConfiguration pipelineConfig, Properties outProps, @Nullable CharacterMapIndex charMapIndex)
            throws XPathException {
            return ((result instanceof StAXResult)
                ? this.makeSequenceNormalizer(new XmlStreamWriterReceiver(Stax2WriterAdapter.wrapIfNecessary(((StAXResult) result).getXMLStreamWriter()),
                    ((SdcctPipelineConfiguration) pipelineConfig)), outProps)
                : super.getReceiver(result, pipelineConfig, outProps, charMapIndex));
        }

        @Override
        protected SequenceReceiver makeSequenceNormalizer(Receiver receiver, Properties props) {
            SequenceReceiver seqNormalizer = super.makeSequenceNormalizer(receiver, props);

            return (((receiver instanceof XmlStreamWriterReceiver) && (seqNormalizer instanceof ComplexContentOutputter))
                ? new SdcctSequenceNormalizer(((XmlStreamWriterReceiver) receiver), ((ComplexContentOutputter) seqNormalizer)) : seqNormalizer);
        }
    }

    @Autowired
    private SdcctSaxParserFactory saxParserFactory;

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

        this.setSerializerFactory(new SdcctSerializerFactory(this));
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
