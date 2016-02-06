package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.ProxyReceiver;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmDestination;

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

    private SdcctConfiguration config;

    public XdmDocumentDestination(SdcctConfiguration config) {
        this.config = config;
    }

    @Override
    public XdmDocument getXdmNode() {
        NodeInfo nodeInfo = super.getXdmNode().getUnderlyingNode();

        return new XdmDocument(((nodeInfo instanceof DocumentInfo) ? ((DocumentInfo) nodeInfo) : new DocumentInfo(nodeInfo)), nodeInfo.getSystemId());
    }

    public XdmDocumentReceiver getReceiver() throws SaxonApiException {
        return this.getReceiver(this.config);
    }

    @Override
    public XdmDocumentReceiver getReceiver(Configuration config) throws SaxonApiException {
        return new XdmDocumentReceiver(this, super.getReceiver(config));
    }

    public SdcctConfiguration getConfiguration() {
        return this.config;
    }
}
