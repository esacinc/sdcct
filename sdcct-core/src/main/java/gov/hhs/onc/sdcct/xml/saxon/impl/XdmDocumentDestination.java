package gov.hhs.onc.sdcct.xml.saxon.impl;

import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.tree.linked.DocumentImpl;

public class XdmDocumentDestination extends XdmDestination {
    public static class XdmDocumentReceiver extends ProxyReceiver {
        private XdmDocumentDestination dest;

        public XdmDocumentReceiver(XdmDocumentDestination dest, Receiver receiver) {
            super(receiver);

            this.dest = dest;
        }

        public XdmDocument getXdmNode() {
            return this.dest.getXdmNode();
        }
    }

    private SdcctSaxonConfiguration config;

    public XdmDocumentDestination(SdcctSaxonConfiguration config) {
        this.setTreeModel((this.config = config).getParseOptions().getModel());
    }

    @Override
    public XdmDocument getXdmNode() {
        DocumentImpl docInfo = ((DocumentImpl) super.getXdmNode().getUnderlyingNode());

        return new XdmDocument(docInfo, docInfo);
    }

    public XdmDocumentReceiver getReceiver() throws SaxonApiException {
        return this.getReceiver(this.config);
    }

    @Override
    public XdmDocumentReceiver getReceiver(Configuration config) throws SaxonApiException {
        return new XdmDocumentReceiver(this, super.getReceiver(config));
    }

    public SdcctSaxonConfiguration getConfiguration() {
        return this.config;
    }
}
