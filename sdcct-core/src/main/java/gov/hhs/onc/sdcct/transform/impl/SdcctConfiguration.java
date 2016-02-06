package gov.hhs.onc.sdcct.transform.impl;

import com.ctc.wstx.sax.WstxSAXParser;
import gov.hhs.onc.sdcct.utils.SdcctOptionUtils;
import gov.hhs.onc.sdcct.xml.impl.SdcctSaxParserFactory;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactoryConfigurationError;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.SaxonOutputKeys;
import net.sf.saxon.lib.SerializerFactory;
import net.sf.saxon.serialize.HTMLIndenter;
import net.sf.saxon.serialize.XMLEmitter;
import net.sf.saxon.serialize.XMLIndenter;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctConfiguration extends Configuration implements InitializingBean {
    private static class SdcctHtmlIndenter extends HTMLIndenter {
        private int indentSpaces;
        private int lineLen;

        public SdcctHtmlIndenter(Receiver nextReceiver, Properties outProps) {
            super(nextReceiver, outProps.getProperty(OutputKeys.METHOD));

            this.indentSpaces = SdcctOptionUtils.getIntegerValue(outProps, SaxonOutputKeys.INDENT_SPACES, super.getIndentation());
            this.lineLen = SdcctOptionUtils.getIntegerValue(outProps, SaxonOutputKeys.LINE_LENGTH, super.getLineLength());
        }

        @Nonnegative
        @Override
        public int getIndentation() {
            return (this.indentSpaces + 1);
        }

        @Nonnegative
        @Override
        public int getLineLength() {
            return this.lineLen;
        }
    }

    private static class SdcctXmlIndenter extends XMLIndenter {
        private int indentSpaces;
        private int lineLen;

        public SdcctXmlIndenter(XMLEmitter nextReceiver, Properties outProps) {
            super(nextReceiver);

            this.setOutputProperties(outProps);

            this.indentSpaces = SdcctOptionUtils.getIntegerValue(outProps, SaxonOutputKeys.INDENT_SPACES, super.getIndentation());
            this.lineLen = SdcctOptionUtils.getIntegerValue(outProps, SaxonOutputKeys.LINE_LENGTH, super.getLineLength());
        }

        @Override
        public void endDocument() throws XPathException {
            this.nextReceiver.endDocument();
        }

        @Nonnegative
        @Override
        public int getIndentation() {
            return (this.indentSpaces + 1);
        }

        @Nonnegative
        @Override
        public int getLineLength() {
            return this.lineLen;
        }
    }

    private class SdcctSerializerFactory extends SerializerFactory {
        public SdcctSerializerFactory() {
            super(SdcctConfiguration.this);
        }

        @Override
        protected ProxyReceiver newXHTMLIndenter(Receiver nextReceiver, Properties outProps) {
            return new SdcctHtmlIndenter(nextReceiver, outProps);
        }

        @Override
        protected ProxyReceiver newHTMLIndenter(Receiver nextReceiver, Properties outProps) {
            return new SdcctHtmlIndenter(nextReceiver, outProps);
        }

        @Override
        protected SdcctXmlIndenter newXMLIndenter(XMLEmitter nextReceiver, Properties outProps) {
            return new SdcctXmlIndenter(nextReceiver, outProps);
        }
    }

    @Autowired
    private SdcctSaxParserFactory saxParserFactory;

    @Override
    public WstxSAXParser makeParser(@Nullable String className) throws TransformerFactoryConfigurationError {
        return this.saxParserFactory.newSAXParser();
    }

    @Override
    public SdcctPipelineConfiguration makePipelineConfiguration() {
        return new SdcctPipelineConfiguration(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setSerializerFactory(new SdcctSerializerFactory());
        this.setSourceParserClass(StringUtils.EMPTY);
        this.setStyleParserClass(StringUtils.EMPTY);
    }

    public void setConfigurationProperties(Map<String, ?> configProps) {
        configProps.forEach(this::setConfigurationProperty);
    }

    public void setXsltVersion(String xsltVersion) {
        this.setConfigurationProperty(FeatureKeys.XSLT_VERSION, xsltVersion);
    }
}
