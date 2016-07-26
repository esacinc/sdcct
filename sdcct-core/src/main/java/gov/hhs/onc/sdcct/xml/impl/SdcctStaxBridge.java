package gov.hhs.onc.sdcct.xml.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import java.util.List;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.sf.saxon.pull.StaxBridge;
import net.sf.saxon.pull.UnparsedEntity;
import net.sf.saxon.trans.XPathException;
import org.codehaus.stax2.ri.Stax2ReaderAdapter;
import org.xml.sax.Locator;

public class SdcctStaxBridge extends StaxBridge {
    private class BridgeXmlStreamReader extends Stax2ReaderAdapter {
        public BridgeXmlStreamReader(XMLStreamReader delegate) {
            super(delegate);
        }

        @Override
        public int next() throws XMLStreamException {
            try {
                return super.next();
            } catch (XMLStreamException e) {
                SdcctStaxBridge.this.cause = e;

                throw e;
            }
        }
    }

    private XMLStreamException cause;

    public SdcctStaxBridge(XMLStreamReader reader) {
        super();

        this.setXMLStreamReader(new BridgeXmlStreamReader(reader));
    }

    @Override
    public int next() throws XPathException {
        this.cause = null;

        try {
            return super.next();
        } catch (XPathException e) {
            Locator loc = e.getLocator();
            e.setLocator(((loc != null) ? new SdcctLocation(loc) : this.getSourceLocator()));

            if (this.cause != null) {
                e.initCause(this.cause);
            }

            throw e;
        }
    }

    @Override
    public SdcctLocation getSourceLocator() {
        Location loc = this.getXMLStreamReader().getLocation();

        return ((loc != null) ? new SdcctLocation(loc) : new SdcctLocation());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public List<UnparsedEntity> getUnparsedEntities() {
        return super.getUnparsedEntities();
    }
}
