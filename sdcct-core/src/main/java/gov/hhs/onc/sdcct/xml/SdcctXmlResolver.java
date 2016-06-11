package gov.hhs.onc.sdcct.xml;

import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.xml.stream.XMLResolver;
import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import net.sf.saxon.trans.XPathException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public interface SdcctXmlResolver extends EntityResolver2, LSResourceResolver, URIResolver, XMLResolver {
    @Nullable
    @Override
    public Source resolve(String href, @Nullable String baseUri) throws XPathException;

    @Nullable
    @Override
    public LSInput resolveResource(String name, String nsUri, @Nullable String publicId, @Nullable String sysId, @Nullable String baseUri);

    @Nullable
    @Override
    public InputSource resolveEntity(@Nullable String publicId, String sysId) throws IOException, SAXException;

    @Nullable
    @Override
    public InputSource resolveEntity(String name, @Nullable String publicId, @Nullable String baseUri, String sysId);

    @Nullable
    public ByteArraySource resolve(@Nullable String nsUri, @Nullable String name, @Nullable String baseUri, @Nullable String publicId, @Nullable String sysId);

    @Nullable
    @Override
    public InputSource getExternalSubset(String name, @Nullable String baseUri) throws IOException, SAXException;
}
