package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.xml.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.impl.SdcctSerializer;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathCompiler;
import gov.hhs.onc.sdcct.xml.xslt.impl.SdcctXsltCompiler;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import net.sf.saxon.s9api.Processor;

public class SdcctProcessor extends Processor {
    public SdcctProcessor(SdcctConfiguration config) {
        super(config);
    }

    @Override
    public SdcctDocumentBuilder newDocumentBuilder() {
        return new SdcctDocumentBuilder(this.getUnderlyingConfiguration());
    }

    @Override
    public SdcctSerializer newSerializer(File outFile) {
        SdcctSerializer serializer = this.newSerializer();
        serializer.setOutputFile(outFile);

        return serializer;
    }

    @Override
    public SdcctSerializer newSerializer(Writer outWriter) {
        SdcctSerializer serializer = this.newSerializer();
        serializer.setOutputWriter(outWriter);

        return serializer;
    }

    @Override
    public SdcctSerializer newSerializer(OutputStream outStream) {
        SdcctSerializer serializer = this.newSerializer();
        serializer.setOutputStream(outStream);

        return serializer;
    }

    @Override
    public SdcctSerializer newSerializer() {
        return new SdcctSerializer(this);
    }

    @Override
    public SdcctXpathCompiler newXPathCompiler() {
        return new SdcctXpathCompiler(this);
    }

    @Override
    public SdcctXsltCompiler newXsltCompiler() {
        return new SdcctXsltCompiler(this);
    }

    @Override
    public SdcctConfiguration getUnderlyingConfiguration() {
        return ((SdcctConfiguration) super.getUnderlyingConfiguration());
    }
}
