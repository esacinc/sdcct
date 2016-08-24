package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctPipelineConfiguration;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import org.codehaus.stax2.XMLStreamWriter2;

public class XmlStreamWriterReceiver extends ReceiverToXMLStreamWriter {
    private XMLStreamWriter2 writer;

    public XmlStreamWriterReceiver(XMLStreamWriter2 writer, SdcctPipelineConfiguration pipelineConfig) {
        super(writer);

        this.writer = writer;

        this.setPipelineConfiguration(pipelineConfig);
    }

    @Override
    public SdcctPipelineConfiguration getPipelineConfiguration() {
        return ((SdcctPipelineConfiguration) super.getPipelineConfiguration());
    }

    public XMLStreamWriter2 getWriter() {
        return this.writer;
    }
}
