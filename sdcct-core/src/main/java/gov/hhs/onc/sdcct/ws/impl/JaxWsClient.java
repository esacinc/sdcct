package gov.hhs.onc.sdcct.ws.impl;

import java.util.concurrent.CountDownLatch;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;

public class JaxWsClient extends AbstractWsClient<Client, Client> {
    public JaxWsClient(Client delegate) {
        super(delegate);
    }

    public Object[] invoke(Client invocationDelegate, QName reqOpQname, Object ... reqParams) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        WsClientInvocationTask<Object[]> task = new WsClientInvocationTask<>(latch, () -> invocationDelegate.invoke(reqOpQname, reqParams));

        ((SdcctConduitSelector) invocationDelegate.getConduitSelector()).getTaskExecutor().execute(task);

        latch.await();

        if (task.hasException()) {
            throw task.getException();
        }

        return task.getResponse();
    }

    @Override
    public Client buildInvocationDelegate() {
        return this.delegate;
    }
}
