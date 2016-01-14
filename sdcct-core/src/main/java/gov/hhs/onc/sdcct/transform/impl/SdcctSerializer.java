package gov.hhs.onc.sdcct.transform.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.FilterFactory;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.ReceivingContentHandler;
import net.sf.saxon.event.Sender;
import net.sf.saxon.lib.AugmentedSource;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.collections4.CollectionUtils;

public class SdcctSerializer extends Serializer {
    private Properties defaultOutProps;

    public SdcctSerializer(SdcctProcessor processor) {
        this(processor, null);
    }

    public SdcctSerializer(SdcctProcessor processor, @Nullable Properties outProps) {
        super(processor);

        if (outProps != null) {
            this.setOutputProperties(outProps);
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
        contentHandler.setReceiver(this.getReceiver(config, pipelineConfig, result, parseOpts, ((outProps != null) ? outProps : this.getOutputProperties())));
        contentHandler.setPipelineConfiguration(pipelineConfig);

        return contentHandler;
    }

    @Override
    public String serializeNodeToString(XdmNode node) throws SaxonApiException {
        return this.serializeToString(node.getUnderlyingNode());
    }

    public void serializeToFile(Source src, File file) throws SaxonApiException {
        this.serializeToFile(src, file, this.getOutputProperties());
    }

    public void serializeToFile(Source src, File file, Properties outProps) throws SaxonApiException {
        try (FileOutputStream outStream = new FileOutputStream(file)) {
            this.serializeToStream(src, outStream, outProps);
        } catch (IOException e) {
            throw new SaxonApiException(String.format("Unable to serialize source (class=%s, sysId=%s) to file (path=%s).", src.getClass().getName(),
                src.getSystemId(), file.getPath()), e);
        }
    }

    public String serializeToString(Source src) throws SaxonApiException {
        return this.serializeToString(src, this.getOutputProperties());
    }

    public String serializeToString(Source src, Properties outProps) throws SaxonApiException {
        byte[] resultBytes = this.serializeToBytes(src, outProps);
        String encName = outProps.getProperty(OutputKeys.ENCODING);

        try {
            return ((encName != null) ? new String(resultBytes, encName) : new String(resultBytes, StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new SaxonApiException(String.format("Unable to serialize source (class=%s, sysId=%s) to string (encName=%s).", src.getClass().getName(),
                src.getSystemId(), encName), e);
        }
    }

    public byte[] serializeToBytes(Source src) throws SaxonApiException {
        return this.serializeToBytes(src, this.getOutputProperties());
    }

    public byte[] serializeToBytes(Source src, Properties outProps) throws SaxonApiException {
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            return this.serializeToStream(src, outStream, outProps).toByteArray();
        } catch (IOException e) {
            throw new SaxonApiException(
                String.format("Unable to serialize source (class=%s, sysId=%s) to bytes.", src.getClass().getName(), src.getSystemId()), e);
        }
    }

    public <T extends OutputStream> T serializeToStream(Source src, T outStream) throws SaxonApiException {
        return this.serializeToStream(src, outStream, this.getOutputProperties());
    }

    public <T extends OutputStream> T serializeToStream(Source src, T outStream, Properties outProps) throws SaxonApiException {
        this.serializeToResult(src, new StreamResult(outStream), outProps);

        return outStream;
    }

    public <T extends Result> T serializeToResult(Source src, T result) throws SaxonApiException {
        return this.serializeToResult(src, result, this.getOutputProperties());
    }

    public <T extends Result> T serializeToResult(Source src, T result, Properties outProps) throws SaxonApiException {
        ParseOptions parseOpts = null;

        if (src instanceof AugmentedSource) {
            AugmentedSource augmentedSrc = ((AugmentedSource) src);

            parseOpts = augmentedSrc.getParseOptions();
        }

        SdcctConfiguration config = this.getProcessor().getUnderlyingConfiguration();
        Receiver receiver = this.getReceiver(config, config.makePipelineConfiguration(), result, parseOpts, outProps);

        try {
            Sender.send(src, receiver, null);

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
    public Receiver getReceiver(Configuration config) throws SaxonApiException {
        return this.getReceiver(config.makePipelineConfiguration());
    }

    @Override
    public Receiver getReceiver(PipelineConfiguration pipelineConfig) throws SaxonApiException {
        return this.getReceiver(((SdcctConfiguration) pipelineConfig.getConfiguration()), ((SdcctPipelineConfiguration) pipelineConfig), this.getResult(),
            null, this.getOutputProperties());
    }

    public Receiver getReceiver(SdcctConfiguration config, SdcctPipelineConfiguration pipelineConfig, Result result, @Nullable ParseOptions parseOpts,
        Properties outProps) throws SaxonApiException {
        Receiver receiver;

        try {
            receiver = config.getSerializerFactory().getReceiver(result, pipelineConfig, outProps);
        } catch (XPathException e) {
            throw new SaxonApiException(String.format("Unable to build Saxon serializer receiver for result (class=%s, sysId=%s).",
                result.getClass().getName(), result.getSystemId()), e);
        }

        if (receiver.getSystemId() == null) {
            receiver.setSystemId(result.getSystemId());
        }

        if (parseOpts != null) {
            ParseOptions pipelineParseOpts = pipelineConfig.getParseOptions();
            pipelineParseOpts.merge(parseOpts);

            List<FilterFactory> parseFilters = pipelineParseOpts.getFilters();

            if (!CollectionUtils.isEmpty(parseFilters)) {
                for (int a = (parseFilters.size() - 1); a >= 0; a--) {
                    receiver = parseFilters.get(a).makeFilter(receiver);
                }

                parseFilters.clear();
            }
        }

        return receiver;
    }

    @Nullable
    public Properties getDefaultOutputProperties() {
        return this.defaultOutProps;
    }

    @Override
    protected void setDefaultOutputProperties(@Nullable Properties defaultOutProps) {
        super.setDefaultOutputProperties((this.defaultOutProps = defaultOutProps));
    }

    @Override
    public Properties getOutputProperties() {
        return super.getOutputProperties();
    }

    public void setOutputProperties(Properties outProps) {
        outProps.stringPropertyNames().forEach(outPropName -> this.setOutputProperty(Property.get(outPropName), outProps.getProperty(outPropName)));
    }

    @Override
    public SdcctProcessor getProcessor() {
        return ((SdcctProcessor) super.getProcessor());
    }
}
