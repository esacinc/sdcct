package gov.hhs.onc.sdcct.xml.impl;

import javax.xml.stream.XMLStreamException;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.StreamWriterToReceiver;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import org.apache.cxf.helpers.MapNamespaceContext;

public class ReceiverXmlStreamWriter extends StreamWriterToReceiver {
    public ReceiverXmlStreamWriter(Receiver receiver) {
        super(receiver);

        try {
            this.setNamespaceContext(new MapNamespaceContext());
        } catch (XMLStreamException e) {
            throw new SaxonApiUncheckedException(e);
        }
    }
}
