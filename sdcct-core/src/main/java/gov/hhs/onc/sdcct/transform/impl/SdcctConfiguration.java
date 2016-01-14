package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.utils.SdcctOptionUtils;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.SequenceReceiver;
import net.sf.saxon.event.StreamWriterToReceiver;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.SaxonOutputKeys;
import net.sf.saxon.lib.SerializerFactory;
import net.sf.saxon.serialize.CharacterMapIndex;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.trans.XPathException;
import org.apache.cxf.staxutils.PrettyPrintXMLStreamWriter;
import org.springframework.beans.factory.InitializingBean;

public class SdcctConfiguration extends Configuration implements InitializingBean {
    private class SdcctSerializerFactory extends SerializerFactory {
        public SdcctSerializerFactory() {
            super(SdcctConfiguration.this);
        }

        @Override
        public SequenceReceiver getReceiver(Result result, PipelineConfiguration pipelineConfig, Properties outProps, @Nullable CharacterMapIndex charMapIndex)
            throws XPathException {
            int indentSpaces = SdcctOptionUtils.getIntegerValue(outProps, SaxonOutputKeys.INDENT_SPACES, -1);
            boolean indent = (SdcctOptionUtils.getBooleanValue(outProps, OutputKeys.INDENT, false) && (indentSpaces > 0));

            outProps.setProperty(OutputKeys.INDENT, SdcctOptionUtils.NO_VALUE);

            SequenceReceiver receiver = super.getReceiver(result, pipelineConfig, outProps, charMapIndex);

            if (indent) {
                Receiver intermediateReceiver =
                    new ReceiverToXMLStreamWriter(new PrettyPrintXMLStreamWriter(new StreamWriterToReceiver(receiver), indentSpaces));
                intermediateReceiver.setPipelineConfiguration(pipelineConfig);

                receiver = new ProxyReceiver(intermediateReceiver);
            }

            return receiver;
        }
    }

    @Override
    public SdcctPipelineConfiguration makePipelineConfiguration() {
        return new SdcctPipelineConfiguration(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setSerializerFactory(new SdcctSerializerFactory());
    }

    public void setConfigurationProperties(Map<String, ?> configProps) {
        configProps.forEach(this::setConfigurationProperty);
    }

    public void setXsltVersion(String xsltVersion) {
        this.setConfigurationProperty(FeatureKeys.XSLT_VERSION, xsltVersion);
    }
}
