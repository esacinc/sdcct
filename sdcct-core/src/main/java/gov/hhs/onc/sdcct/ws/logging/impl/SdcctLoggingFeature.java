package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.logging.LoggingEvent;
import gov.hhs.onc.sdcct.logging.MdcPropertyNames;
import gov.hhs.onc.sdcct.net.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.net.logging.impl.HttpRequestEventImpl;
import gov.hhs.onc.sdcct.net.logging.impl.HttpResponseEventImpl;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.Headers;
import org.apache.cxf.ws.policy.PolicyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class SdcctLoggingFeature extends AbstractFeature {
    private abstract class AbstractLoggingInterceptor<T extends WsEvent> extends AbstractPhaseInterceptor<Message> {
        protected Supplier<T> eventCreator;

        protected AbstractLoggingInterceptor(String phase, Supplier<T> eventCreator) {
            super(phase);

            this.eventCreator = eventCreator;
        }

        @Override
        public void handleMessage(Message msg) throws Fault {
            if (msg.containsKey(WsPropertyNames.WS_TX_ID)) {
                return;
            }

            Exchange exchange = msg.getExchange();
            T event = this.buildEvent(exchange, msg);

            try {
                this.handleMessageInternal(exchange, msg, event);
            } catch (Fault e) {
                throw e;
            } catch (Exception e) {
                throw new Fault(new Exception(String.format("Unable to handle CXF message logging event (txId=%s).", event.getTxId()), e));
            }
        }

        protected abstract void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception;

        protected T buildEvent(Exchange exchange, Message msg) {
            T event = this.eventCreator.get();
            event.setEndpointAddress(exchange.getEndpoint().getEndpointInfo().getAddress());

            return event;
        }
    }

    private abstract class AbstractLoggingInInterceptor<T extends WsEvent> extends AbstractLoggingInterceptor<T> {
        protected AbstractLoggingInInterceptor(Supplier<T> eventCreator) {
            super(Phase.RECEIVE, eventCreator);

            this.addBefore(PolicyConstants.POLICY_IN_INTERCEPTOR_ID);
        }

        @Override
        protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
            InputStream msgInStream = msg.getContent(InputStream.class);
            DelegatingInputStream delegatingMsgInStream = ((msgInStream instanceof DelegatingInputStream) ? ((DelegatingInputStream) msgInStream) : null);
            CachedOutputStream cachedMsgOutStream = new CachedOutputStream();

            IOUtils.copy(((delegatingMsgInStream != null) ? delegatingMsgInStream.getInputStream() : msgInStream), cachedMsgOutStream);

            msgInStream.close();

            cachedMsgOutStream.flush();

            byte[] msgPayloadContent = cachedMsgOutStream.getBytes();

            msgInStream = new ByteArrayInputStream(msgPayloadContent);

            if (delegatingMsgInStream != null) {
                delegatingMsgInStream.setInputStream(msgInStream);
            } else {
                msg.setContent(InputStream.class, msgInStream);
            }

            cachedMsgOutStream.close();

            String msgEnc = SdcctWsUtils.getProperty(msg, Message.ENCODING);

            event.setPayload(((msgEnc != null) ? new String(msgPayloadContent, msgEnc) : new String(msgPayloadContent, StandardCharsets.UTF_8)));

            SdcctLoggingFeature.this.log(event);
        }

        @Override
        protected T buildEvent(Exchange exchange, Message msg) {
            T event = super.buildEvent(exchange, msg);
            event.setDirection(WsDirection.INBOUND);

            return event;
        }
    }

    private class ServerLoggingInInterceptor extends AbstractLoggingInInterceptor<WsRequestEvent> {
        public ServerLoggingInInterceptor() {
            super(WsRequestEventImpl::new);
        }

        @Override
        protected WsRequestEvent buildEvent(Exchange exchange, Message msg) {
            WsRequestEvent event = super.buildEvent(exchange, msg);
            event.setEndpointType(RestEndpointType.SERVER);
            event.setTxId(buildTxId(exchange, msg, WsPropertyNames.WS_TX_ID, () -> MDC.get(MdcPropertyNames.WS_TX_ID)));

            return event;
        }
    }

    private class ClientLoggingInInterceptor extends AbstractLoggingInInterceptor<WsResponseEvent> {
        public ClientLoggingInInterceptor() {
            super(WsResponseEventImpl::new);
        }

        @Override
        protected void handleMessageInternal(Exchange exchange, Message msg, WsResponseEvent event) throws Exception {
            HttpResponseEvent httpEvent = new HttpResponseEventImpl();
            httpEvent.setContentType(SdcctWsUtils.getProperty(msg, Message.CONTENT_TYPE));
            httpEvent.setEndpointType(RestEndpointType.CLIENT);
            httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));
            httpEvent.setStatusCode(SdcctWsUtils.getProperty(msg, Message.RESPONSE_CODE, Integer.class));
            httpEvent.setTxId(buildTxId(exchange, msg, WsPropertyNames.HTTP_TX_ID));

            SdcctLoggingFeature.this.log(httpEvent);

            super.handleMessageInternal(exchange, msg, event);
        }

        @Override
        protected WsResponseEvent buildEvent(Exchange exchange, Message msg) {
            WsResponseEvent event = super.buildEvent(exchange, msg);
            event.setEndpointType(RestEndpointType.CLIENT);
            event.setTxId(buildTxId(exchange, msg, WsPropertyNames.WS_TX_ID));

            return event;
        }
    }

    private abstract class AbstractLoggingOutputStreamCallback<T extends WsEvent> implements CachedOutputStreamCallback {
        protected Exchange exchange;
        protected Message msg;
        protected T event;

        protected AbstractLoggingOutputStreamCallback(Exchange exchange, Message msg, T event) {
            this.exchange = exchange;
            this.msg = msg;
            this.event = event;
        }

        @Override
        public void onClose(CachedOutputStream msgPayloadOutStream) {
            try {
                byte[] msgPayloadContent = msgPayloadOutStream.getBytes();
                String msgEnc = SdcctWsUtils.getProperty(msg, Message.ENCODING);

                event.setPayload(((msgEnc != null) ? new String(msgPayloadContent, msgEnc) : new String(msgPayloadContent, StandardCharsets.UTF_8)));

                SdcctLoggingFeature.this.log(event);
            } catch (Fault e) {
                throw e;
            } catch (Exception e) {
                throw new Fault(e);
            }
        }

        @Override
        public void onFlush(CachedOutputStream msgPayloadOutStream) {
        }
    }

    private class ServerLoggingOutputStreamCallback extends AbstractLoggingOutputStreamCallback<WsResponseEvent> {
        public ServerLoggingOutputStreamCallback(Exchange exchange, Message msg, WsResponseEvent event) {
            super(exchange, msg, event);
        }
    }

    private class ClientLoggingOutputStreamCallback extends AbstractLoggingOutputStreamCallback<WsRequestEvent> {
        public ClientLoggingOutputStreamCallback(Exchange exchange, Message msg, WsRequestEvent event) {
            super(exchange, msg, event);
        }

        @Override
        public void onClose(CachedOutputStream msgPayloadOutStream) {
            try {
                HttpRequestEvent httpEvent = new HttpRequestEventImpl();
                // httpEvent.setContentLength(SdcctWsUtils.getProperty(msg, PhizWsMessageProperties.CONTENT_LEN, Long.class));
                httpEvent.setContentType(SdcctWsUtils.getProperty(msg, Message.CONTENT_TYPE));
                httpEvent.setEndpointType(RestEndpointType.CLIENT);
                httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));
                httpEvent.setMethod(SdcctWsUtils.getProperty(msg, Message.HTTP_REQUEST_METHOD));
                httpEvent.setPathInfo(SdcctWsUtils.getProperty(msg, Message.PATH_INFO));
                // httpEvent.setProtocol(SdcctWsUtils.getProperty(msg, PhizWsMessageProperties.PROTOCOL));
                httpEvent.setQueryString(SdcctWsUtils.getProperty(msg, Message.QUERY_STRING));
                // httpEvent.setScheme(PhizWsUtils.getProperty(msg, PhizWsMessageProperties.HTTP_SCHEME));
                httpEvent.setTxId(buildTxId(exchange, msg, WsPropertyNames.HTTP_TX_ID, () -> MDC.get(MdcPropertyNames.HTTP_TX_ID)));
                httpEvent.setUri(SdcctWsUtils.getProperty(msg, Message.REQUEST_URI));
                httpEvent.setUrl(SdcctWsUtils.getProperty(msg, Message.REQUEST_URL));

                SdcctLoggingFeature.this.log(httpEvent);
            } catch (Fault e) {
                throw e;
            } catch (Exception e) {
                throw new Fault(e);
            }

            super.onClose(msgPayloadOutStream);
        }
    }

    private abstract class AbstractLoggingOutInterceptor<T extends WsEvent, U extends AbstractLoggingOutputStreamCallback<T>>
        extends AbstractLoggingInterceptor<T> {
        public AbstractLoggingOutInterceptor(Supplier<T> eventCreator) {
            super(Phase.PRE_STREAM, eventCreator);

            this.addBefore(StaxOutInterceptor.class.getName());
        }

        @Override
        protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
            CacheAndWriteOutputStream msgPayloadOutStream = new CacheAndWriteOutputStream(msg.getContent(OutputStream.class));
            msgPayloadOutStream.registerCallback(this.buildOutputStreamCallback(exchange, msg, event));
            msg.setContent(OutputStream.class, msgPayloadOutStream);
        }

        protected abstract U buildOutputStreamCallback(Exchange exchange, Message msg, T event);

        @Override
        protected T buildEvent(Exchange exchange, Message msg) {
            T event = super.buildEvent(exchange, msg);
            event.setDirection(WsDirection.OUTBOUND);

            return event;
        }
    }

    private class ServerLoggingOutInterceptor extends AbstractLoggingOutInterceptor<WsResponseEvent, ServerLoggingOutputStreamCallback> {
        public ServerLoggingOutInterceptor() {
            super(WsResponseEventImpl::new);
        }

        @Override
        protected ServerLoggingOutputStreamCallback buildOutputStreamCallback(Exchange exchange, Message msg, WsResponseEvent event) {
            return new ServerLoggingOutputStreamCallback(exchange, msg, event);
        }

        @Override
        protected WsResponseEvent buildEvent(Exchange exchange, Message msg) {
            WsResponseEvent event = super.buildEvent(exchange, msg);
            event.setEndpointType(RestEndpointType.SERVER);
            event.setTxId(buildTxId(exchange, msg, WsPropertyNames.WS_TX_ID));

            return event;
        }
    }

    private class ClientLoggingOutInterceptor extends AbstractLoggingOutInterceptor<WsRequestEvent, ClientLoggingOutputStreamCallback> {
        public ClientLoggingOutInterceptor() {
            super(WsRequestEventImpl::new);
        }

        @Override
        protected ClientLoggingOutputStreamCallback buildOutputStreamCallback(Exchange exchange, Message msg, WsRequestEvent event) {
            return new ClientLoggingOutputStreamCallback(exchange, msg, event);
        }

        @Override
        protected WsRequestEvent buildEvent(Exchange exchange, Message msg) {
            WsRequestEvent event = super.buildEvent(exchange, msg);
            event.setEndpointType(RestEndpointType.CLIENT);
            event.setTxId(buildTxId(exchange, msg, WsPropertyNames.WS_TX_ID, () -> MDC.get(MdcPropertyNames.WS_TX_ID)));

            return event;
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctLoggingFeature.class);

    @Override
    public void initialize(Server server, Bus bus) {
        Endpoint endpoint = server.getEndpoint();

        initialize(endpoint, new ServerLoggingInInterceptor(), new ServerLoggingOutInterceptor());
    }

    @Override
    public void initialize(Client client, Bus bus) {
        initialize(client, new ClientLoggingInInterceptor(), new ClientLoggingOutInterceptor());
    }

    private static String buildTxId(Exchange exchange, Message msg, String wsPropName) {
        return buildTxId(exchange, msg, wsPropName, null);
    }

    @Nullable
    private static String buildTxId(Exchange exchange, Message msg, String wsPropName, @Nullable Supplier<String> supplier) {
        if (msg.containsKey(wsPropName)) {
            return SdcctWsUtils.getProperty(msg, wsPropName);
        }

        String txId = null;

        if (exchange.containsKey(wsPropName)) {
            txId = SdcctWsUtils.getProperty(exchange, wsPropName);
        } else if (supplier != null) {
            exchange.put(wsPropName, (txId = supplier.get()));
        }

        if (txId != null) {
            msg.put(wsPropName, txId);
        }

        return txId;
    }

    private static void initialize(InterceptorProvider prov, AbstractLoggingInInterceptor<?> inInterceptor,
        AbstractLoggingOutInterceptor<?, ?> outInterceptor) {
        prov.getInInterceptors().add(inInterceptor);
        prov.getInFaultInterceptors().add(inInterceptor);
        prov.getOutInterceptors().add(outInterceptor);
        prov.getOutFaultInterceptors().add(outInterceptor);
    }

    private void log(LoggingEvent event) {
        LOGGER.info(event.buildMarker(), StringUtils.EMPTY);
    }
}
