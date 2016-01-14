package gov.hhs.onc.sdcct.transform.impl;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.Processor;

public class SdcctProcessor extends Processor {
    public SdcctProcessor(SdcctConfiguration config) {
        super(config);
    }

    @Override
    public SdcctSerializer newSerializer(File outFile) {
        return this.newSerializer(outFile, null);
    }

    public SdcctSerializer newSerializer(File outFile, @Nullable Properties outProps) {
        SdcctSerializer serializer = this.newSerializer(outProps);
        serializer.setOutputFile(outFile);

        return serializer;
    }

    @Override
    public SdcctSerializer newSerializer(Writer outWriter) {
        return this.newSerializer(outWriter, null);
    }

    public SdcctSerializer newSerializer(Writer outWriter, @Nullable Properties outProps) {
        SdcctSerializer serializer = this.newSerializer(outProps);
        serializer.setOutputWriter(outWriter);

        return serializer;
    }

    @Override
    public SdcctSerializer newSerializer(OutputStream outStream) {
        return this.newSerializer(outStream, null);
    }

    public SdcctSerializer newSerializer(OutputStream outStream, @Nullable Properties outProps) {
        SdcctSerializer serializer = this.newSerializer(outProps);
        serializer.setOutputStream(outStream);

        return serializer;
    }

    @Override
    public SdcctSerializer newSerializer() {
        return this.newSerializer(((Properties) null));
    }

    public SdcctSerializer newSerializer(@Nullable Properties outProps) {
        SdcctSerializer serializer = new SdcctSerializer(this, outProps);
        serializer.setDefaultOutputProperties(this.getUnderlyingConfiguration().getDefaultSerializationProperties());

        return serializer;
    }

    @Override
    public SdcctConfiguration getUnderlyingConfiguration() {
        return ((SdcctConfiguration) super.getUnderlyingConfiguration());
    }
}
