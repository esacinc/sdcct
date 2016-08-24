package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.beans.LocationBean;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.transform.impl.SdcctPipelineConfiguration;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import net.sf.saxon.evpull.PullEvent;
import net.sf.saxon.evpull.StaxToEventBridge;
import net.sf.saxon.trans.XPathException;
import org.xml.sax.Locator;

/**
 * TODO: Remove exception cause holding once the changes from <a href="https://saxonica.plan.io/issues/2900">Saxon bug #2900</a> are available.
 */
public class XmlStreamReaderEventIterator extends StaxToEventBridge implements LocationBean {
    private class ExceptionCauseXmlStreamReader extends StreamReaderDelegate {
        public ExceptionCauseXmlStreamReader(XMLStreamReader delegate) {
            super(delegate);
        }

        @Override
        public int next() throws XMLStreamException {
            try {
                return super.next();
            } catch (XMLStreamException e) {
                XmlStreamReaderEventIterator.this.cause = e;

                throw e;
            }
        }
    }

    private XMLStreamException cause;

    public XmlStreamReaderEventIterator(XMLStreamReader reader, SdcctPipelineConfiguration pipelineConfig) {
        this.setXMLStreamReader(new ExceptionCauseXmlStreamReader(reader));
        this.setPipelineConfiguration(pipelineConfig);
    }

    @Nullable
    @Override
    public PullEvent next() throws XPathException {
        this.cause = null;

        try {
            return super.next();
        } catch (XPathException e) {
            Locator locator = e.getLocator();
            e.setLocator(((locator != null) ? new SdcctLocation(locator) : new SdcctLocation(this.getXMLStreamReader().getLocation())));

            if (this.cause != null) {
                e.initCause(this.cause);
            }

            throw e;
        }
    }

    @Nullable
    @Override
    public String getSystemId(int locId) {
        return super.getSystemId(locId);
    }

    public boolean hasColumnNumber() {
        return (this.getColumnNumber() > 0);
    }

    public boolean hasLineNumber() {
        return (this.getLineNumber() > 0);
    }

    @Override
    public SdcctPipelineConfiguration getPipelineConfiguration() {
        return ((SdcctPipelineConfiguration) super.getPipelineConfiguration());
    }

    public boolean hasPublicId() {
        return (this.getPublicId() != null);
    }

    @Nullable
    @Override
    public String getPublicId() {
        return super.getPublicId();
    }

    public boolean hasSystemId() {
        return (this.getSystemId() != null);
    }

    @Nullable
    @Override
    public String getSystemId() {
        return super.getSystemId();
    }
}
