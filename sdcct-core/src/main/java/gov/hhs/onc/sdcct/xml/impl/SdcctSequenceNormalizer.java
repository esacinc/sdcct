package gov.hhs.onc.sdcct.xml.impl;

import net.sf.saxon.event.ComplexContentOutputter;
import net.sf.saxon.event.ProxyReceiver;

public class SdcctSequenceNormalizer extends ProxyReceiver {
    private XmlStreamWriterReceiver writerReceiver;

    public SdcctSequenceNormalizer(XmlStreamWriterReceiver writerReceiver, ComplexContentOutputter nextReceiver) {
        super(nextReceiver);

        this.writerReceiver = writerReceiver;
    }

    public XmlStreamWriterReceiver getWriterReceiver() {
        return this.writerReceiver;
    }
}
