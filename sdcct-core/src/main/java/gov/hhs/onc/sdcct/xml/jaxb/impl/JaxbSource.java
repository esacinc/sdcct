package gov.hhs.onc.sdcct.xml.jaxb.impl;

import com.ctc.wstx.sax.SAXFeature;
import com.ctc.wstx.sax.SAXProperty;
import com.github.sebhoss.warnings.CompilerWarnings;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

public class JaxbSource<T> extends SAXSource implements XMLReader {
    private final static SAXFeature NAMESPACE_PREFIXES_SAX_FEATURE = SAXFeature.findBySuffix("namespace-prefixes");
    private final static SAXFeature NAMESPACES_SAX_FEATURE = SAXFeature.findBySuffix("namespaces");

    private Marshaller marshaller;
    private Class<T> srcClass;
    private Object src;
    private XMLFilterImpl filter = new XMLFilterImpl();

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public JaxbSource(Marshaller marshaller, T src) {
        this(marshaller, ((Class<T>) src.getClass()), src);
    }

    public JaxbSource(Marshaller marshaller, JAXBElement<T> src) {
        this(marshaller, src.getDeclaredType(), src);
    }

    private JaxbSource(Marshaller marshaller, Class<T> srcClass, Object src) {
        super(new InputSource());

        this.marshaller = marshaller;
        this.srcClass = srcClass;
        this.src = src;

        this.setXMLReader(this);
    }

    @Override
    public void parse(InputSource src) throws IOException, SAXException {
        this.parse();
    }

    @Override
    public void parse(String sysId) throws IOException, SAXException {
        this.parse();
    }

    public void parse() throws SAXException {
        try {
            this.marshaller.marshal(this.src, this.filter);
        } catch (JAXBException e) {
            SAXParseException saxParseException =
                new SAXParseException(String.format("Unable to marshal JAXB source object (class=%s).", this.src.getClass()), null, null, -1, -1, e);

            if (this.hasErrorHandler()) {
                // noinspection ConstantConditions
                this.getErrorHandler().fatalError(saxParseException);
            }

            throw saxParseException;
        }
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXFeature feature = SAXFeature.findByUri(name);

        if (feature == NAMESPACES_SAX_FEATURE) {
            return true;
        } else if (feature == NAMESPACE_PREFIXES_SAX_FEATURE) {
            return false;
        } else {
            throw new SAXNotRecognizedException(name);
        }
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXFeature feature = SAXFeature.findByUri(name);

        if (!((feature == NAMESPACES_SAX_FEATURE) && value) && (feature != NAMESPACE_PREFIXES_SAX_FEATURE)) {
            throw new SAXNotRecognizedException(name);
        }
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (SAXProperty.findByUri(name) == SAXProperty.LEXICAL_HANDLER) {
            return null;
        } else {
            throw new SAXNotRecognizedException(name);
        }
    }

    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (SAXProperty.findByUri(name) != SAXProperty.LEXICAL_HANDLER) {
            throw new SAXNotRecognizedException(name);
        }
    }

    @Override
    public ContentHandler getContentHandler() {
        return this.filter.getContentHandler();
    }

    @Override
    public void setContentHandler(ContentHandler handler) {
        this.filter.setContentHandler(handler);
    }

    @Override
    public DTDHandler getDTDHandler() {
        return this.filter.getDTDHandler();
    }

    @Override
    public void setDTDHandler(DTDHandler handler) {
        this.filter.setDTDHandler(handler);
    }

    @Override
    public EntityResolver getEntityResolver() {
        return this.filter.getEntityResolver();
    }

    @Override
    public void setEntityResolver(EntityResolver resolver) {
        this.filter.setEntityResolver(resolver);
    }

    public boolean hasErrorHandler() {
        return (this.getErrorHandler() != null);
    }

    @Nullable
    @Override
    public ErrorHandler getErrorHandler() {
        return this.filter.getErrorHandler();
    }

    @Override
    public void setErrorHandler(@Nullable ErrorHandler handler) {
        this.filter.setErrorHandler(handler);
    }
}
