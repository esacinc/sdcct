package gov.hhs.onc.sdcct.xml.saxon.impl;

import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctPullEventSource;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory;
import java.io.File;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.evpull.StaxToEventBridge;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.linked.DocumentImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctDocumentBuilder extends DocumentBuilder {
    public final static String BEAN_NAME = "docBuilder";

    public final static String BUILD_METHOD_NAME = "build";

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    private SdcctSaxonConfiguration config;

    public SdcctDocumentBuilder(SdcctSaxonConfiguration config) {
        super(config);

        this.setLineNumbering((this.config = config).isLineNumbering());
        this.setTreeModel(this.config.getParseOptions().getModel());
    }

    @Override
    public XdmDocument build(File srcFile) throws SaxonApiException {
        return ((XdmDocument) super.build(srcFile));
    }

    @Override
    public XdmDocument build(Source src) throws SaxonApiException {
        String srcPublicId = SdcctTransformUtils.getPublicId(src), srcSysId = src.getSystemId();
        Source buildSrc = src;

        if (buildSrc instanceof StreamSource) {
            StaxToEventBridge srcEventIterator = new StaxToEventBridge();
            srcEventIterator.setPipelineConfiguration(this.config.makePipelineConfiguration());

            try {
                srcEventIterator.setXMLStreamReader(this.xmlInFactory.createXMLStreamReader(buildSrc));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }

            buildSrc = new SdcctPullEventSource(srcPublicId, srcSysId, srcEventIterator);
        }

        try {
            return new XdmDocument(src, ((DocumentImpl) this.config.buildDocumentTree(buildSrc, this.config.getParseOptions()).getRootNode()));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }
}
