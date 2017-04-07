package gov.hhs.onc.sdcct.web.gateway.testcases.ihe.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.sdcct.net.SdcctSchemes;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormManagerTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.utils.SdcctDateUtils;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebItTests;
import gov.hhs.onc.sdcct.web.testcases.ihe.websocket.IheTestcaseResultMessage;
import gov.hhs.onc.sdcct.web.websocket.SdcctWebSocketPaths;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(enabled = false, groups = { "sdcct.test.web.gateway.all", "sdcct.test.it.web.gateway.all", "sdcct.test.it.web.gateway.testcases.all",
    "sdcct.test.it.web.gateway.testcases.ihe.all", "sdcct.test.it.web.gateway.testcases.ihe.controller" })
public class IheTestcaseControllerWebItTests extends AbstractSdcctWebItTests {
    @Value("${sdcct.ws.form.archiver.rfd.url}")
    private String rfdFormArchiverEndpointAddr;

    @Value("${sdcct.ws.form.manager.rfd.url}")
    private String rfdFormManagerEndpointAddr;

    @Value("${sdcct.ws.form.receiver.rfd.url}")
    private String rfdFormReceiverEndpointAddr;

    @Value("${sdcct.testcases.ihe.form.archiver.process.url}")
    private String iheFormArchiverProcessUrl;

    @Value("${sdcct.testcases.ihe.form.manager.process.url}")
    private String iheFormManagerProcessUrl;

    @Value("${sdcct.testcases.ihe.form.receiver.process.url}")
    private String iheFormReceiverProcessUrl;

    @Value("${sdcct.testcases.ihe.results.websocket.url}")
    private String iheTestcaseResultsWebsocketUrl;

    @Resource(name = "iheTestcase1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private IheFormManagerTestcase testcase;

    @Resource(name = "websocketClientStompLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private WebSocketStompClient webSocketStompClient;

    @Resource(name = "objMapperTestcases")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper objMapper;

    private CountDownLatch webSocketClientLatch;
    private Throwable webSocketClientException;
    private StompSession webSocketStompSession;
    private IheTestcaseResultMessage testcaseResultMsg;

    @Test(enabled = false)
    public void testProcessTestcases() throws Exception {
        RestTemplate restTemplate = new RestTemplateBuilder().messageConverters(new ByteArrayHttpMessageConverter(),
            new MappingJackson2HttpMessageConverter(this.objMapper), new StringHttpMessageConverter(StandardCharsets.UTF_8)).build();

        this.webSocketClientLatch = new CountDownLatch(1);

        IheFormManagerTestcaseSubmission testcaseSubmission =
            new IheFormManagerTestcaseSubmissionImpl(this.testcase, this.rfdFormManagerEndpointAddr, this.testcase.getFormIds().get(0));

        restTemplate.postForObject(this.iheFormManagerProcessUrl, testcaseSubmission, Object.class);

        this.webSocketClientLatch.await((SdcctDateUtils.MS_IN_SEC * 10), TimeUnit.MILLISECONDS);

        Assertions.assertThat(this.webSocketClientException).isNull();

        Assertions.assertThat(this.testcaseResultMsg).isNotNull();
        Assertions.assertThat(this.testcaseResultMsg.getPayload().hasResponse()).isTrue();
    }

    @BeforeClass(enabled = false)
    public void initializeWebSocketClient() throws Exception {
        this.webSocketStompClient.start();

        this.webSocketClientLatch = new CountDownLatch(1);

        this.webSocketStompClient.connect((SdcctSchemes.WS + StringUtils.removeStart(this.iheTestcaseResultsWebsocketUrl, SdcctSchemes.HTTP)),
            new StompSessionHandlerAdapter() {
                @Override
                public void handleException(StompSession session, StompCommand cmd, StompHeaders headers, byte[] payload, Throwable exception) {
                    IheTestcaseControllerWebItTests.this.webSocketClientException = exception;

                    IheTestcaseControllerWebItTests.this.webSocketClientLatch.countDown();
                }

                @Override
                public void handleTransportError(StompSession session, Throwable exception) {
                    IheTestcaseControllerWebItTests.this.webSocketClientException = exception;

                    IheTestcaseControllerWebItTests.this.webSocketClientLatch.countDown();
                }

                @Override
                public void afterConnected(StompSession session, StompHeaders headers) {
                    (IheTestcaseControllerWebItTests.this.webSocketStompSession = session).setAutoReceipt(true);

                    IheTestcaseControllerWebItTests.this.webSocketClientLatch.countDown();
                }
            });

        this.webSocketClientLatch.await((SdcctDateUtils.MS_IN_SEC * 5), TimeUnit.MILLISECONDS);

        Assertions.assertThat(this.webSocketClientException).isNull();
        Assertions.assertThat(this.webSocketStompSession).isNotNull();
        Assertions.assertThat(this.webSocketStompSession.isConnected()).isTrue();

        this.webSocketClientLatch = new CountDownLatch(1);

        this.webSocketStompSession.subscribe(SdcctWebSocketPaths.TOPIC_TESTCASES_IHE_RESULTS, new StompFrameHandler() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                IheTestcaseControllerWebItTests.this.testcaseResultMsg = ((IheTestcaseResultMessage) payload);

                IheTestcaseControllerWebItTests.this.webSocketClientLatch.countDown();
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return IheTestcaseResultMessage.class;
            }
        });

        this.webSocketClientLatch.await((SdcctDateUtils.MS_IN_SEC * 5), TimeUnit.MILLISECONDS);

        Assertions.assertThat(this.webSocketClientException).isNull();
    }
}
