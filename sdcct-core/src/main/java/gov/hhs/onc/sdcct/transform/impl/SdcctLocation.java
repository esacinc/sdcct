package gov.hhs.onc.sdcct.transform.impl;

import javax.annotation.Nullable;
import javax.xml.stream.Location;
import javax.xml.transform.SourceLocator;
import net.sf.saxon.expr.parser.ExplicitLocation;
import net.sf.saxon.om.NodeInfo;
import org.codehaus.stax2.XMLStreamLocation2;
import org.w3c.dom.DOMLocator;
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
    private Node node;
    private NodeInfo nodeInfo;

    public SdcctLocation() {
        this(null, -1, -1);
    }

    public SdcctLocation(NodeInfo nodeInfo) {
        this(nodeInfo.getSystemId(), nodeInfo.getLineNumber(), nodeInfo.getColumnNumber());

        this.nodeInfo = nodeInfo;
    }

    public SdcctLocation(net.sf.saxon.expr.parser.Location loc) {
        this(loc.getPublicId(), loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber());
    }

    public SdcctLocation(javax.xml.transform.dom.DOMLocator domLoc) {
        this(domLoc.getPublicId(), domLoc.getSystemId(), domLoc.getLineNumber(), domLoc.getColumnNumber());

        this.node = domLoc.getOriginatingNode();
    }

    public SdcctLocation(SourceLocator srcLoc) {
        this(srcLoc.getPublicId(), srcLoc.getSystemId(), srcLoc.getLineNumber(), srcLoc.getColumnNumber());
    }

    public SdcctLocation(DOMLocator domLoc) {
        this(null, domLoc.getUri(), domLoc.getLineNumber(), domLoc.getColumnNumber(), domLoc.getByteOffset());

        this.node = domLoc.getRelatedNode();
    }

    public SdcctLocation(XMLStreamLocation2 loc) {
        this(loc.getPublicId(), loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber(), loc.getCharacterOffset());

        this.context = loc.getContext();
    }

    public SdcctLocation(SAXParseException cause) {
        this(cause.getPublicId(), cause.getSystemId(), cause.getLineNumber(), cause.getColumnNumber());
    }

    public SdcctLocation(Locator srcLoc) {
        this(srcLoc.getPublicId(), srcLoc.getSystemId(), srcLoc.getLineNumber(), srcLoc.getColumnNumber());
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

    public boolean hasNode() {
        return (this.node != null);
    }

    @Nullable
    public Node getNode() {
        return this.node;
    }

    public void setNode(@Nullable Node node) {
        this.node = node;
    }

    public boolean hasNodeInfo() {
        return (this.nodeInfo != null);
    }

    @Nullable
    public NodeInfo getNodeInfo() {
        return this.nodeInfo;
    }

    public void setNodeInfo(@Nullable NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
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
