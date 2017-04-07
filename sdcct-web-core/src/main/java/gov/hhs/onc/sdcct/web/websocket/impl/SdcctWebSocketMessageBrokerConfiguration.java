package gov.hhs.onc.sdcct.web.websocket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.sdcct.web.websocket.SdcctWebSocketPaths;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.Validator;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

@Configuration("configurationWebSocketMsgBroker")
@DependsOn({ "embeddedServletContainerFactoryTomcat", "websocketContainerServer" })
public class SdcctWebSocketMessageBrokerConfiguration extends WebSocketMessageBrokerConfigurationSupport {
    @Value("${sdcct.websocket.sockjs.client.lib.url}")
    private String sockJsClientLibUrl;

    @Value("${sdcct.websocket.sockjs.disconnect.delay}")
    private long sockJsDisconnectDelay;

    @Value("${sdcct.websocket.sockjs.heartbeat.time}")
    private long sockJsHeartbeatTime;

    @Value("${sdcct.websocket.sockjs.http.msg.cache.size}")
    private int sockJsHttpMsgCacheSize;

    @Value("${sdcct.websocket.sockjs.stream.limit}")
    private int sockJsStreamLimit;

    @Value("${sdcct.websocket.msg.broker.cache.size}")
    private int msgBrokerCacheSize;

    @Resource(name = "taskExecutorWebSocketMsgBrokerChannelClientInbound")
    private ThreadPoolTaskExecutor msgBrokerInboundClientChannelTaskExecutor;

    @Resource(name = "taskExecutorWebSocketMsgBrokerChannelClientOutbound")
    private ThreadPoolTaskExecutor msgBrokerOutboundClientChannelTaskExecutor;

    @Resource(name = "taskSchedulerWebSocketMsgBroker")
    private ThreadPoolTaskScheduler msgBrokerTaskScheduler;

    @Resource(name = "taskExecutorWebSocketMsgBrokerChannel")
    private ThreadPoolTaskExecutor msgBrokerChannelTaskExecutor;

    @Resource(name = "objMapperTestcases")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper objMapper;

    @Autowired
    private Validator validator;

    @Override
    protected void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint(SdcctWebSocketPaths.TESTCASES_IHE_RESULTS)
            .setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy())).addInterceptors(new HttpSessionHandshakeInterceptor())
            .withSockJS().setClientLibraryUrl(this.sockJsClientLibUrl).setDisconnectDelay(this.sockJsDisconnectDelay).setHeartbeatTime(this.sockJsHeartbeatTime)
            .setHttpMessageCacheSize(this.sockJsHttpMsgCacheSize).setMessageCodec(new Jackson2SockJsMessageCodec(this.objMapper))
            .setStreamBytesLimit(this.sockJsStreamLimit);
    }

    @Override
    protected void configureMessageBroker(MessageBrokerRegistry msgBrokerRegistry) {
        msgBrokerRegistry.setApplicationDestinationPrefixes(SdcctWebSocketPaths.APP_PREFIX).setCacheLimit(this.msgBrokerCacheSize)
            .enableSimpleBroker(SdcctWebSocketPaths.TOPIC_PREFIX);
    }

    @Override
    public ThreadPoolTaskExecutor clientOutboundChannelExecutor() {
        return this.msgBrokerOutboundClientChannelTaskExecutor;
    }

    @Override
    public ThreadPoolTaskExecutor clientInboundChannelExecutor() {
        return this.msgBrokerInboundClientChannelTaskExecutor;
    }

    @Override
    public ThreadPoolTaskScheduler messageBrokerTaskScheduler() {
        return this.msgBrokerTaskScheduler;
    }

    @Override
    public ThreadPoolTaskExecutor brokerChannelExecutor() {
        return this.msgBrokerChannelTaskExecutor;
    }

    @Override
    protected MappingJackson2MessageConverter createJacksonConverter() {
        DefaultContentTypeResolver contentTypeResolver = new DefaultContentTypeResolver();
        contentTypeResolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        MappingJackson2MessageConverter msgConv = new MappingJackson2MessageConverter();
        msgConv.setContentTypeResolver(contentTypeResolver);
        msgConv.setObjectMapper(this.objMapper);

        return msgConv;
    }

    @Override
    public Validator getValidator() {
        return this.validator;
    }
}
