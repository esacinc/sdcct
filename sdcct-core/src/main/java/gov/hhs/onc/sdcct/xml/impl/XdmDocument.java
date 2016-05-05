package gov.hhs.onc.sdcct.xml.impl;

import javax.annotation.Nullable;
import javax.xml.transform.dom.DOMSource;
import net.sf.saxon.dom.DocumentOverNodeInfo;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.DocumentURI;
import net.sf.saxon.s9api.XdmNode;

public class XdmDocument extends XdmNode {
    private DocumentOverNodeInfo doc;
    private DOMSource src;
    private DocumentURI uri;

    public XdmDocument(DocumentInfo docInfo) {
        this(docInfo, null);
    }

    public XdmDocument(DocumentInfo docInfo, @Nullable String sysId) {
        super(docInfo);

        this.src = new DOMSource((this.doc = ((DocumentOverNodeInfo) NodeOverNodeInfo.wrap(docInfo))), sysId);
    }

    public DocumentOverNodeInfo getDocument() {
        return this.doc;
    }

    public DOMSource getSource() {
        return this.src;
    }

    @Override
    public DocumentInfo getUnderlyingNode() {
        return ((DocumentInfo) super.getUnderlyingNode());
    }

    public boolean hasUri() {
        return (this.uri != null);
    }

    @Nullable
    public DocumentURI getUri() {
        return this.uri;
    }

    public void setUri(@Nullable DocumentURI uri) {
        this.uri = uri;
    }
}
