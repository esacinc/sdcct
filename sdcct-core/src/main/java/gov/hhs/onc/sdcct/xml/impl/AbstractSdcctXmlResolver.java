package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.ResourceSourceResolver;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.xml.SdcctXmlResolver;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import net.sf.saxon.lib.StandardURIResolver;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.trans.XPathException;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class AbstractSdcctXmlResolver extends StandardURIResolver implements SdcctXmlResolver {
    protected ResourceSourceResolver resourceSrcResolver;

    protected AbstractSdcctXmlResolver(ResourceSourceResolver resourceSrcResolver) {
        super();

        this.resourceSrcResolver = resourceSrcResolver;
    }

    @Nullable
    @Override
    public Source resolve(String href, @Nullable String baseUri) throws XPathException {
        return this.resolve(null, null, baseUri, null, href);
    }

    @Nullable
    @Override
    public LSInput resolveResource(String name, String nsUri, @Nullable String publicId, @Nullable String sysId, @Nullable String baseUri) {
        ByteArraySource src = this.resolve(nsUri, name, baseUri, publicId, sysId);

        return ((src != null) ? src.getLsInput() : null);
    }

    @Nullable
    @Override
    public InputSource resolveEntity(@Nullable String publicId, String sysId) throws IOException, SAXException {
        ByteArraySource src = this.resolve(null, null, null, publicId, sysId);

        return ((src != null) ? src.getInputSource() : null);
    }

    @Nullable
    @Override
    public InputSource resolveEntity(String name, @Nullable String publicId, @Nullable String baseUri, String sysId) {
        ByteArraySource src = this.resolve(null, name, baseUri, publicId, sysId);

        return ((src != null) ? src.getInputSource() : null);
    }

    @Nullable
    @Override
    public ByteArraySource resolve(@Nullable String nsUri, @Nullable String name, @Nullable String baseUri, @Nullable String publicId, @Nullable String sysId) {
        try {
            ByteArraySource src = this.resourceSrcResolver.resolve(sysId);

            if (src != null) {
                src.setSystemId(sysId);
            }

            return src;
        } catch (IOException e) {
            throw new SaxonApiUncheckedException(e);
        }
    }

    @Nullable
    @Override
    public InputSource getExternalSubset(String name, @Nullable String baseUri) throws IOException, SAXException {
        return null;
    }
}
