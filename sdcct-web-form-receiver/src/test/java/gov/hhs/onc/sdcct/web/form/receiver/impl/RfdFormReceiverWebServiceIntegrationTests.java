package gov.hhs.onc.sdcct.web.form.receiver.impl;

import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.form.receiver.all", "sdcct.test.it.web.form.receiver.ws.rfd" })
public class RfdFormReceiverWebServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
    // @formatter:off
    /*
    @Resource(name = "formTest1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Form testForm1;

    @Resource(name = "clientFormReceiverRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private JaxWsClient testClient;

    @Test
    public void testSubmitForm() throws Exception {
        this.testClient.invoke(this.testClient.buildInvocationDelegate(), RfdWsXmlQnames.SUBMIT_FORM_OP,
            new AnyXMLContentTypeImpl(Collections.singletonList(this.testForm1.getPackageElement())));
    }
    */
    // @formatter:on
}
