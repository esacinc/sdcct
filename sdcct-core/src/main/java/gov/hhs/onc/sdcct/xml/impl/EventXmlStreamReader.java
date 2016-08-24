package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import javax.annotation.Nullable;
import javax.xml.transform.SourceLocator;
import net.sf.saxon.evpull.EventIterator;
import net.sf.saxon.evpull.EventToStaxBridge;
import net.sf.saxon.evpull.PullEvent;
import net.sf.saxon.evpull.StartElementEvent;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.trans.XPathException;

public class EventXmlStreamReader extends EventToStaxBridge {
    private static class BufferedEventIterator implements EventIterator {
        private EventIterator delegate;
        private PullEvent event;

        public BufferedEventIterator(EventIterator delegate) {
            this.delegate = delegate;
        }

        @Nullable
        @Override
        public PullEvent next() throws XPathException {
            this.event = null;

            return (this.event = this.delegate.next());
        }

        public boolean hasEvent() {
            return (this.event != null);
        }

        @Nullable
        public PullEvent getEvent() {
            return this.event;
        }

        @Override
        public boolean isFlatSequence() {
            return this.delegate.isFlatSequence();
        }
    }

    public EventXmlStreamReader(EventIterator prov) {
        super(new BufferedEventIterator(prov), null);
    }

    @Override
    public SdcctLocation getLocation() {
        BufferedEventIterator prov = ((BufferedEventIterator) this.getProvider());

        if (!prov.hasEvent()) {
            return new SdcctLocation();
        }

        PullEvent event = prov.getEvent();

        if (event instanceof NodeInfo) {
            return new SdcctLocation(((NodeInfo) event));
        } else if (event instanceof StartElementEvent) {
            return new SdcctLocation(((SourceLocator) ((StartElementEvent) event).getLocation()));
        } else {
            return new SdcctLocation();
        }
    }
}
