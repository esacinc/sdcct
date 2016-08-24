package gov.hhs.onc.sdcct.xml.impl;

import net.sf.saxon.Configuration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.trans.XPathException;

public class ReceiverDestination<T extends Receiver> implements Destination {
    private T receiver;

    public ReceiverDestination(T receiver) {
        this.receiver = receiver;
    }

    @Override
    public void close() throws SaxonApiException {
        try {
            this.receiver.close();
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }

    @Override
    public T getReceiver(Configuration config) throws SaxonApiException {
        return this.receiver;
    }

    public T getReceiver() {
        return this.receiver;
    }
}
