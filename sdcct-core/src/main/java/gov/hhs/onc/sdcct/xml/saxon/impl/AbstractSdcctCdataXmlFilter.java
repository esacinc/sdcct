package gov.hhs.onc.sdcct.xml.saxon.impl;

import gov.hhs.onc.sdcct.transform.saxon.SdcctReceiverOptions;
import javax.xml.stream.XMLStreamException;
import net.sf.saxon.event.ComplexContentOutputter;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.ReceiverOptions;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.SchemaType;
import org.codehaus.stax2.XMLStreamWriter2;
import org.codehaus.stax2.ri.Stax2WriterAdapter;

public abstract class AbstractSdcctCdataXmlFilter extends ProxyReceiver {
    protected XMLStreamWriter2 writer;
    protected boolean inCdataElem;

    protected AbstractSdcctCdataXmlFilter(Receiver nextReceiver) {
        super(nextReceiver);

        Receiver receiver = this.nextReceiver;

        while (!(receiver instanceof ReceiverToXMLStreamWriter)) {
            receiver = ((receiver instanceof ComplexContentOutputter)
                ? ((ComplexContentOutputter) receiver).getReceiver() : ((ProxyReceiver) receiver).getUnderlyingReceiver());
        }

        this.writer = Stax2WriterAdapter.wrapIfNecessary(((ReceiverToXMLStreamWriter) receiver).getXMLStreamWriter());
    }

    @Override
    public void characters(CharSequence chars, Location loc, int props) throws XPathException {
        if (this.inCdataElem) {
            props |= ReceiverOptions.DISABLE_ESCAPING;
        }

        if ((props & ReceiverOptions.DISABLE_ESCAPING) != 0) {
            String str = chars.toString();

            try {
                if ((props & SdcctReceiverOptions.USE_CDATA) != 0) {
                    this.writer.writeCData(str);
                } else {
                    this.writer.writeRaw(str);
                }
            } catch (XMLStreamException e) {
                throw XPathException.makeXPathException(e);
            }
        } else {
            super.characters(chars, loc, (props & ~SdcctReceiverOptions.USE_CDATA));
        }
    }

    @Override
    public void endElement() throws XPathException {
        this.inCdataElem = false;

        super.endElement();
    }

    @Override
    public void startElement(NodeName elemName, SchemaType type, Location loc, int props) throws XPathException {
        if (this.isCdataElement(elemName, type, loc, props)) {
            this.inCdataElem = true;
        }

        super.startElement(elemName, type, loc, props);
    }

    protected abstract boolean isCdataElement(NodeName elemName, SchemaType type, Location loc, int props);
}
