package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.Configuration;
import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.SaxonApiException;
import org.codehaus.stax2.XMLStreamWriter2;

public class SdcctXmlStreamWriterDestination implements Destination {
    private XMLStreamWriter2 writer;

    public SdcctXmlStreamWriterDestination(XMLStreamWriter2 writer) {
        this.writer = writer;
    }

    @Override
    public void close() throws SaxonApiException {
        try {
            this.writer.flush();
            this.writer.close();
        } catch (XMLStreamException e) {
            throw new SaxonApiException(e);
        }
    }

    @Override
    public XmlStreamWriterReceiver getReceiver(Configuration config) throws SaxonApiException {
        return new XmlStreamWriterReceiver(this.writer, ((SdcctConfiguration) config).makePipelineConfiguration());
    }

    public XMLStreamWriter getWriter() {
        return this.writer;
    }
}
