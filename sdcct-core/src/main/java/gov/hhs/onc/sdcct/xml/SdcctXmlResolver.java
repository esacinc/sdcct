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
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public interface SdcctXmlResolver extends Cloneable, EntityResolver, LSResourceResolver, URIResolver, XMLResolver {
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
    public Source resolveEntity(@Nullable String publicId, String sysId, @Nullable String baseUri, @Nullable String nsUri);

    @Nullable
    public ByteArraySource resolve(@Nullable String nsUri, @Nullable String name, @Nullable String baseUri, @Nullable String publicId, @Nullable String sysId);

    public void addStaticSource(@Nullable String nsUri, @Nullable String publicId, @Nullable String sysId, ByteArraySource src);

    public SdcctXmlResolver clone();
}
