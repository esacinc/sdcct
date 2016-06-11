package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import java.io.File;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.dom.DOMNodeWrapper;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.WhitespaceStrippingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctDocumentBuilder extends DocumentBuilder {
    public final static String BEAN_NAME = "docBuilder";

    public final static String BUILD_METHOD_NAME = "build";

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    public SdcctDocumentBuilder(SdcctConfiguration config) {
        super(config);

        this.setLineNumbering(config.isLineNumbering());
        this.setTreeModel(config.getParseOptions().getModel());
        this.setWhitespaceStrippingPolicy(WhitespaceStrippingPolicy.NONE);
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
                buildSrc = new StAXSource(this.xmlInFactory.createXMLStreamReader(src));
            } catch (XMLStreamException e) {
                throw new SaxonApiException(e);
            }
        }

        return new XdmDocument(src, ((DOMNodeWrapper) super.build(buildSrc).getUnderlyingNode()));
    }
}
