package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.logging.impl.SdcctMarkerBuilder;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;

public class WsRequestEventImpl extends AbstractWsEvent implements WsRequestEvent {
    @Override
    protected SdcctMarkerBuilder buildMarkerInternal() {
        String msgPrefix = String.format("Web service %s request", this.endpointType.name().toLowerCase());

        return super.buildMarkerInternal().appendMessage(
            (msgPrefix + String.format(" (txId=%s, direction=%s, endpointAddr=%s, soapHeaders=%s, soapFault=%s):\n%s", this.txId, this.direction.name(),
                this.endpointAddr, this.soapHeaders, this.soapFault, (this.hasPrettyPayload() ? this.prettyPayload : this.payload))),
            (msgPrefix + SdcctStringUtils.PERIOD_CHAR));
    }
}
