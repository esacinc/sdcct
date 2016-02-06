package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import java.io.File;
import javax.xml.transform.Source;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.SaxonApiException;

public class SdcctDocumentBuilder extends DocumentBuilder {
    public SdcctDocumentBuilder(SdcctConfiguration config) {
        super(config);
    }

    @Override
    public XdmDocument build(File srcFile) throws SaxonApiException {
        return ((XdmDocument) super.build(srcFile));
    }

    @Override
    public XdmDocument build(Source src) throws SaxonApiException {
        NodeInfo nodeInfo = super.build(src).getUnderlyingNode();

        return new XdmDocument(((nodeInfo instanceof DocumentInfo) ? ((DocumentInfo) nodeInfo) : new DocumentInfo(nodeInfo)), src.getSystemId());
    }
}
