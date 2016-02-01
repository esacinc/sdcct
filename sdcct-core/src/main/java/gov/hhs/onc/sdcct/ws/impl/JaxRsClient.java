package gov.hhs.onc.sdcct.ws.impl;

import java.util.concurrent.CountDownLatch;
import javax.annotation.Nullable;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.http.HttpMethod;

public class JaxRsClient extends AbstractWsClient<Client, WebClient> {
    public JaxRsClient(Client delegate) {
        super(delegate);
    }

    public Response invoke(WebClient invocationDelegate, HttpMethod reqMethod) throws Exception {
        return this.invoke(invocationDelegate, reqMethod, null);
    }

    public Response invoke(WebClient invocationDelegate, HttpMethod reqMethod, @Nullable Object reqEntity) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        WsClientInvocationTask<Response> task = new WsClientInvocationTask<>(latch, () -> invocationDelegate.invoke(reqMethod.name(), reqEntity));

        ((SdcctConduitSelector) WebClient.getConfig(invocationDelegate).getConduitSelector()).getTaskExecutor().execute(task);

        latch.await();

        if (task.hasException()) {
            throw task.getException();
        }

        return task.getResponse();
    }

    @Override
    public WebClient buildInvocationDelegate() {
        return WebClient.fromClient(this.delegate);
    }
}
