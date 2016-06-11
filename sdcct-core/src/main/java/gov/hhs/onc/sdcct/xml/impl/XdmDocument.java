package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import net.sf.saxon.dom.DOMNodeWrapper;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.s9api.XdmNode;
import org.w3c.dom.Document;

public class XdmDocument extends XdmNode {
    private Document doc;
    private Source src;
    private String publicId;
    private String sysId;
    private SdcctDocumentUri docUri;

    public XdmDocument(Source src, DOMNodeWrapper docInfo) {
        this(src, src.getSystemId(), docInfo);
    }

    public XdmDocument(Source src, @Nullable String sysId, DOMNodeWrapper docInfo) {
        this(src, SdcctTransformUtils.getPublicId(src), sysId, null, docInfo);
    }

    public XdmDocument(Source src, @Nullable String sysId, @Nullable SdcctDocumentUri docUri, DOMNodeWrapper docInfo) {
        this(src, SdcctTransformUtils.getPublicId(src), sysId, docUri, docInfo);
    }

    public XdmDocument(Source src, @Nullable String publicId, @Nullable String sysId, @Nullable SdcctDocumentUri docUri, DOMNodeWrapper docInfo) {
        super(docInfo);

        this.src = src;
        this.doc = ((Document) docInfo.getRealNode());

        Location docLoc = docInfo.getRoot().saveLocation();

        this.publicId = ((publicId != null) ? publicId : docLoc.getPublicId());
        this.sysId = ((sysId != null) ? sysId : docLoc.getSystemId());
        this.docUri = ((docUri != null) ? docUri : ((this.sysId != null) ? new SdcctDocumentUri(this.sysId) : null));
    }

    public Document getDocument() {
        return this.doc;
    }

    public boolean hasDocumentUri() {
        return (this.docUri != null);
    }

    @Nullable
    public SdcctDocumentUri getDocumentUri() {
        return this.docUri;
    }

    public void setDocUri(@Nullable SdcctDocumentUri docUri) {
        this.docUri = docUri;
    }

    public boolean hasPublicId() {
        return (this.publicId != null);
    }

    @Nullable
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
    public String getSystemId() {
        return this.sysId;
    }

    public void setSystemId(@Nullable String sysId) {
        this.sysId = sysId;
    }

    @Override
    public DOMNodeWrapper getUnderlyingNode() {
        return ((DOMNodeWrapper) super.getUnderlyingNode());
    }
}
