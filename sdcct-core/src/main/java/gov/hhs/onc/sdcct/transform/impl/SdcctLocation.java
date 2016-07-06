package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.transform.content.path.ContentPath;
import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.transform.SourceLocator;
import net.sf.saxon.expr.parser.ExplicitLocation;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.tree.AttributeLocation;
import net.sf.saxon.type.Type;
import org.codehaus.stax2.XMLStreamLocation2;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMLocator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

public class SdcctLocation extends ExplicitLocation implements Location, XMLStreamLocation2 {
    private String publicId;
    private String sysId;
    private int lineNum;
    private int colNum;
    private int charOffset;
    private XMLStreamLocation2 context;
    private QName attrQname;
    private QName elemQname;
    private ContentPath contentPath;

    public SdcctLocation() {
        this(null, -1, -1);
    }

    public SdcctLocation(NodeInfo nodeInfo) {
        this(nodeInfo.getSystemId(), nodeInfo.getLineNumber(), nodeInfo.getColumnNumber());

        this.initializeNodeInfo(nodeInfo);
    }

    public SdcctLocation(net.sf.saxon.expr.parser.Location loc) {
        this(loc.getPublicId(), loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber());

        this.initializeAttributeLocation(loc);
    }

    public SdcctLocation(javax.xml.transform.dom.DOMLocator domLoc) {
        this(domLoc.getPublicId(), domLoc.getSystemId(), domLoc.getLineNumber(), domLoc.getColumnNumber());

        this.initializeNode(domLoc.getOriginatingNode());
    }

    public SdcctLocation(SourceLocator locator) {
        this(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());

        this.initializeAttributeLocation(locator);
    }

    public SdcctLocation(DOMLocator locator) {
        this(null, locator.getUri(), locator.getLineNumber(), locator.getColumnNumber(), locator.getByteOffset());

        this.initializeNode(locator.getRelatedNode());
    }

    public SdcctLocation(XMLStreamLocation2 loc) {
        this(loc.getPublicId(), loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber(), loc.getCharacterOffset());

        this.context = loc.getContext();
    }

    public SdcctLocation(SAXParseException cause) {
        this(cause.getPublicId(), cause.getSystemId(), cause.getLineNumber(), cause.getColumnNumber());
    }

    public SdcctLocation(Locator locator) {
        this(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());

        this.initializeAttributeLocation(locator);
    }

    public SdcctLocation(Location loc) {
        this(loc.getPublicId(), loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber(), loc.getCharacterOffset());
    }

    public SdcctLocation(@Nullable String sysId, int lineNum, int colNum) {
        this(null, sysId, lineNum, colNum);
    }

    public SdcctLocation(@Nullable String publicId, @Nullable String sysId, int lineNum, int colNum) {
        this(publicId, sysId, lineNum, colNum, -1);
    }

    public SdcctLocation(@Nullable String publicId, @Nullable String sysId, int lineNum, int colNum, int charOffset) {
        super(null, -1, -1);

        this.publicId = publicId;
        this.sysId = sysId;
        this.lineNum = lineNum;
        this.colNum = colNum;
        this.charOffset = charOffset;
    }

    @Override
    public SdcctLocation saveLocation() {
        return this;
    }

    private void initializeAttributeLocation(@Nullable Object loc) {
        if ((loc == null) || !(loc instanceof AttributeLocation)) {
            return;
        }

        AttributeLocation attrLoc = ((AttributeLocation) loc);
        StructuredQName nodeQname = attrLoc.getAttributeName();

        if (nodeQname != null) {
            this.attrQname = new QName(nodeQname);
        }

        if ((nodeQname = attrLoc.getElementName()) != null) {
            this.elemQname = new QName(nodeQname);
        }
    }

    private void initializeNodeInfo(@Nullable NodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }

        int nodeKind = nodeInfo.getNodeKind();
        NodeInfo elemInfo = null;

        if (nodeKind == Type.ATTRIBUTE) {
            this.attrQname = new QName(nodeInfo.getPrefix(), nodeInfo.getURI(), nodeInfo.getLocalPart());

            elemInfo = nodeInfo.getParent();
        } else if (nodeKind == Type.ELEMENT) {
            elemInfo = nodeInfo;
        }

        if (elemInfo == null) {
            return;
        }

        this.elemQname = new QName(elemInfo.getPrefix(), elemInfo.getURI(), elemInfo.getLocalPart());
    }

    private void initializeNode(@Nullable Node node) {
        if (node == null) {
            return;
        }

        Element elem = null;

        if (node instanceof Attr) {
            Attr attr = ((Attr) node);

            this.attrQname = new QName(attr.getPrefix(), null, attr.getLocalName());

            elem = attr.getOwnerElement();
        } else if (node instanceof Element) {
            elem = ((Element) node);
        }

        if (elem == null) {
            return;
        }

        this.elemQname = new QName(elem.getPrefix(), null, elem.getLocalName());
    }

    public boolean hasAttributeQname() {
        return (this.attrQname != null);
    }

    @Nullable
    public QName getAttributeQname() {
        return this.attrQname;
    }

    public void setAttributeQname(@Nullable QName attrQname) {
        this.attrQname = attrQname;
    }

    public boolean hasCharacterOffset() {
        return (this.charOffset >= 0);
    }

    @Override
    public int getCharacterOffset() {
        return this.charOffset;
    }

    public void setCharacterOffset(int charOffset) {
        this.charOffset = charOffset;
    }

    public boolean hasColumnNumber() {
        return (this.colNum >= 0);
    }

    @Override
    public int getColumnNumber() {
        return this.colNum;
    }

    public void setColumnNumber(int colNum) {
        this.colNum = colNum;
    }

    public boolean hasContentPath() {
        return (this.contentPath != null);
    }

    @Nullable
    public ContentPath getContentPath() {
        return this.contentPath;
    }

    public void setContentPath(@Nullable ContentPath contentPath) {
        this.contentPath = contentPath;
    }

    public boolean hasContext() {
        return (this.context != null);
    }

    @Nullable
    @Override
    public XMLStreamLocation2 getContext() {
        return this.context;
    }

    public void setContext(@Nullable XMLStreamLocation2 context) {
        this.context = context;
    }

    public boolean hasElementQname() {
        return (this.elemQname != null);
    }

    @Nullable
    public QName getElementQname() {
        return this.elemQname;
    }

    public void setElementQname(@Nullable QName elemQname) {
        this.elemQname = elemQname;
    }

    public boolean hasLineNumber() {
        return (this.lineNum >= 0);
    }

    @Override
    public int getLineNumber() {
        return this.lineNum;
    }

    public void setLineNumber(int lineNum) {
        this.lineNum = lineNum;
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
}
