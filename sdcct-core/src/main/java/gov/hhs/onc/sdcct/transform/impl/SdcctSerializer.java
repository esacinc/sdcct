package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.config.utils.SdcctPropertiesUtils;
import gov.hhs.onc.sdcct.io.impl.ByteArrayResult;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.FilterFactory;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.ReceivingContentHandler;
import net.sf.saxon.event.Sender;
import net.sf.saxon.event.SequenceReceiver;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import org.apache.commons.collections4.CollectionUtils;

public class SdcctSerializer extends Serializer {
    private Properties defaultOutProps;

    public SdcctSerializer(SdcctProcessor processor) {
        super(processor);

        this.setDefaultOutputProperties(SdcctPropertiesUtils.clone(processor.getUnderlyingConfiguration().getDefaultSerializationProperties()));
    }

    public byte[] serialize(Source src, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        return this.serialize(src, new ByteArrayResult(), parseOpts, outProps).getBytes();
    }

    public <T extends Result> T serialize(Source src, T result, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        SdcctConfiguration config = this.getProcessor().getUnderlyingConfiguration();
        SequenceReceiver receiver = this.getReceiver(config, config.makePipelineConfiguration(), result, parseOpts, outProps);

        try {
            if (src instanceof NodeInfo) {
                SequenceIterator srcIterator = SingletonIterator.makeIterator(((NodeInfo) src));
                Item srcItem;

                receiver.open();

                while ((srcItem = srcIterator.next()) != null) {
                    receiver.append(srcItem);
                }
            } else {
                Sender.send(src, receiver, null);
            }

            return result;
        } catch (XPathException e) {
            throw new SaxonApiException(String.format("Unable to serialize source (class=%s, sysId=%s) to result (class=%s, sysId=%s).", src.getClass()
                .getName(), src.getSystemId(), result.getClass().getName(), result.getSystemId()), e);
        } finally {
            try {
                receiver.close();
            } catch (XPathException e) {
                // noinspection ThrowFromFinallyBlock
                throw new SaxonApiException(String.format("Unable to close Saxon serializer receiver (class=%s) for result (class=%s, sysId=%s).", receiver
                    .getClass().getName(), result.getClass().getName(), result.getSystemId()), e);
            }
        }
    }

    @Override
    public ReceivingContentHandler getContentHandler() throws SaxonApiException {
        return this.getContentHandler(this.getResult(), null, null);
    }

    public ReceivingContentHandler getContentHandler(Result result, @Nullable ParseOptions parseOpts, @Nullable Properties outProps) throws SaxonApiException {
        SdcctConfiguration config = this.getProcessor().getUnderlyingConfiguration();
        SdcctPipelineConfiguration pipelineConfig = config.makePipelineConfiguration();

        ReceivingContentHandler contentHandler = new ReceivingContentHandler();
        contentHandler.setReceiver(this.getReceiver(config, pipelineConfig, result, parseOpts, outProps));
        contentHandler.setPipelineConfiguration(pipelineConfig);

        return contentHandler;
    }

    @Override
    public SequenceReceiver getReceiver(Configuration config) throws SaxonApiException {
        return this.getReceiver(config.makePipelineConfiguration());
    }

    @Override
    public SequenceReceiver getReceiver(PipelineConfiguration pipelineConfig) throws SaxonApiException {
        return this.getReceiver(((SdcctConfiguration) pipelineConfig.getConfiguration()), ((SdcctPipelineConfiguration) pipelineConfig), this.getResult(),
            null, null);
    }

    public SequenceReceiver getReceiver(SdcctConfiguration config, SdcctPipelineConfiguration pipelineConfig, Result result, @Nullable ParseOptions parseOpts,
        @Nullable Properties outProps) throws SaxonApiException {
        SequenceReceiver receiver;

        try {
            receiver = config.getSerializerFactory().getReceiver(result, pipelineConfig, this.mergeOutputProperties(outProps));
        } catch (XPathException e) {
            throw new SaxonApiException(String.format("Unable to build Saxon serializer receiver for result (class=%s, sysId=%s).",
                result.getClass().getName(), result.getSystemId()), e);
        }

        if (receiver.getSystemId() == null) {
            receiver.setSystemId(result.getSystemId());
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
            mergedOutProps.putAll(outProps);
        }

        return mergedOutProps;
    }

    @Nullable
    public Properties getDefaultOutputProperties() {
        return this.defaultOutProps;
    }

    @Override
    public void setDefaultOutputProperties(@Nullable Properties defaultOutProps) {
        super.setDefaultOutputProperties((this.defaultOutProps = defaultOutProps));
    }

    @Override
    public SdcctProcessor getProcessor() {
        return ((SdcctProcessor) super.getProcessor());
    }
}
