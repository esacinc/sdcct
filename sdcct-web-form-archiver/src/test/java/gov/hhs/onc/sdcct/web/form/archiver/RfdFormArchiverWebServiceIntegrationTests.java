package gov.hhs.onc.sdcct.web.form.archiver;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.rfd.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.impl.AnyXMLContentTypeImpl;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import org.testng.annotations.Test;

import java.util.Collections;

import javax.annotation.Resource;

@Test(groups = { "sdcct.test.it.web.form.receiver.all", "sdcct.test.it.web.form.receiver.ws.rfd" })
public class RfdFormArchiverWebServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
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
}
