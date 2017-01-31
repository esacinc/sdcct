package gov.hhs.onc.sdcct.xml.saxon.impl;

import gov.hhs.onc.sdcct.beans.PublicIdentifiedBean;
import gov.hhs.onc.sdcct.beans.SystemIdentifiedBean;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import net.sf.saxon.dom.DocumentOverNodeInfo;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.tree.linked.DocumentImpl;
import net.sf.saxon.tree.linked.ElementImpl;
import net.sf.saxon.tree.util.Navigator;

public class XdmDocument extends XdmNode implements PublicIdentifiedBean, SystemIdentifiedBean {
    private DocumentOverNodeInfo doc;
    private Source src;
    private String publicId;
    private String sysId;
    private SdcctDocumentUri docUri;

    public XdmDocument(Source src, DocumentImpl docInfo) {
        this(src, src.getSystemId(), docInfo);
    }

    public XdmDocument(Source src, @Nullable String sysId, DocumentImpl docInfo) {
        this(src, SdcctTransformUtils.getPublicId(src), sysId, null, docInfo);
    }

    public XdmDocument(Source src, @Nullable String sysId, @Nullable SdcctDocumentUri docUri, DocumentImpl docInfo) {
        this(src, SdcctTransformUtils.getPublicId(src), sysId, docUri, docInfo);
    }

    public XdmDocument(Source src, @Nullable String publicId, @Nullable String sysId, @Nullable SdcctDocumentUri docUri, DocumentImpl docInfo) {
        super(docInfo);

        this.src = src;
        this.doc = ((DocumentOverNodeInfo) NodeOverNodeInfo.wrap(docInfo));

        Location docLoc = docInfo.getRoot().saveLocation();

        this.publicId = ((publicId != null) ? publicId : docLoc.getPublicId());
        this.sysId = ((sysId != null) ? sysId : docLoc.getSystemId());
        this.docUri = ((docUri != null) ? docUri : ((this.sysId != null) ? new SdcctDocumentUri(this.sysId) : null));
    }

    public DocumentOverNodeInfo getDocument() {
        return this.doc;
    }

    public ElementImpl getDocumentElement() {
        return ((ElementImpl) Navigator.getOutermostElement(this.getUnderlyingNode()));
    }

    public boolean hasDocumentUri() {
        return (this.docUri != null);
    }

    @Nullable
    public SdcctDocumentUri getDocumentUri() {
        return this.docUri;
    }

    public void setDocumentUri(@Nullable SdcctDocumentUri docUri) {
        this.docUri = docUri;
    }

    public boolean hasPublicId() {
        return (this.publicId != null);
    }

    @Nullable
    @Override
    public String getPublicId() {
        return this.publicId;
    }

    public void setPublicId(@Nullable String publicId) {
        this.publicId = publicId;
    }

    public Source getSource() {
        return this.src;
    }

    public boolean hasSystemId() {
        return (this.sysId != null);
    }

    @Nullable
    @Override
    public String getSystemId() {
        return this.sysId;
    }

    public void setSystemId(@Nullable String sysId) {
        this.sysId = sysId;
    }

    @Override
    public DocumentImpl getUnderlyingNode() {
        return ((DocumentImpl) super.getUnderlyingNode());
    }
}
