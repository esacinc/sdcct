package gov.hhs.onc.sdcct.transform.impl;

import com.ctc.wstx.sax.WstxSAXParser;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import gov.hhs.onc.sdcct.xml.impl.SdcctSaxParserFactory;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stax.StAXSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.dom.DOMObjectModel;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctConfiguration extends Configuration implements InitializingBean {
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

        this.getParseOptions().setModel(DOMObjectModel.getInstance());
    }

    public void setConfigurationProperties(Map<String, ?> configProps) {
        configProps.forEach(this::setConfigurationProperty);
    }

    @Override
    public void setDefaultSerializationProperties(Properties defaultOutProps) {
        super.setDefaultSerializationProperties(SdcctTransformUtils.buildOutputProperties(defaultOutProps));
    }

    public void setXsltVersion(String xsltVersion) {
        this.setConfigurationProperty(FeatureKeys.XSLT_VERSION, xsltVersion);
    }
}
