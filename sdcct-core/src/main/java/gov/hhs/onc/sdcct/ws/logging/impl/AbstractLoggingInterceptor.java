package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import java.util.function.Supplier;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

public abstract class AbstractLoggingInterceptor<T extends WsEvent> extends AbstractPhaseInterceptor<Message> {
    protected SdcctLoggingFeature feature;
    protected Class<T> eventClass;
    protected Supplier<T> eventCreator;
    protected WsDirection direction;
    protected RestEndpointType endpointType;
    protected String txIdPropName;
    protected WsMessageType msgType;

    protected AbstractLoggingInterceptor(SdcctLoggingFeature feature, String phase, Class<T> eventClass, Supplier<T> eventCreator, WsDirection direction,
        RestEndpointType endpointType, String txIdPropName, WsMessageType msgType) {
        super(phase);

        this.feature = feature;
        this.eventClass = eventClass;
        this.eventCreator = eventCreator;
        this.direction = direction;
        this.endpointType = endpointType;
        this.txIdPropName = txIdPropName;
        this.msgType = msgType;
    }

    @Override
    public void handleMessage(Message msg) throws Fault {
        Exchange exchange = msg.getExchange();
        T event;
        boolean initializeEvent = false;

        if (!SdcctWsPropertyUtils.containsKey(msg, this.eventClass)) {
            msg.put(this.eventClass, this.eventCreator.get());

            initializeEvent = true;
        }

        String txId = SdcctLoggingFeature.buildTxId(exchange, msg, (event = msg.get(this.eventClass)), this.txIdPropName);

        this.buildEvent(exchange, msg, event, initializeEvent);

        try {
            this.handleMessageInternal(exchange, msg, event);
        } catch (Fault e) {
            throw e;
        } catch (Exception e) {
            throw new Fault(new Exception(String.format("Unable to handle CXF message event (txId=%s) logging.", txId), e));
        }
    }

    protected abstract void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception;

    protected T buildEvent(Exchange exchange, Message msg, T event, boolean initialize) {
        if (initialize) {
            event.setDirection(this.direction);
            event.setEndpointType(this.endpointType);
            event.setMessageType(this.msgType);
        }

        return event;
    }
}
