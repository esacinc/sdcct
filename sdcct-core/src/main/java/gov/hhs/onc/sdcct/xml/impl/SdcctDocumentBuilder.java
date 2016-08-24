package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.transform.impl.SdcctPullEventSource;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import java.io.File;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
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

    private SdcctConfiguration config;

    public SdcctDocumentBuilder(SdcctConfiguration config) {
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
        Source buildSrc = src;

        if (src instanceof StreamSource) {
            try {
                buildSrc = new SdcctPullEventSource(SdcctTransformUtils.getPublicId(src), src.getSystemId(),
                    new XmlStreamReaderEventIterator(this.xmlInFactory.createXMLStreamReader(src), this.config.makePipelineConfiguration()));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        try {
            return new XdmDocument(src, ((DocumentImpl) this.config.buildDocumentTree(buildSrc, this.config.getParseOptions()).getRootNode()));
        } catch (XPathException e) {
            throw new SaxonApiException(e);
        }
    }
}
