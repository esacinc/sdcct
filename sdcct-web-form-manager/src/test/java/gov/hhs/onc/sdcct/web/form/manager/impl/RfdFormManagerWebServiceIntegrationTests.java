package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.rfd.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormRequestTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.WorkflowDataTypeImpl;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import javax.annotation.Resource;
import org.testng.annotations.Test;

@Test(enabled = false, groups = { "sdcct.test.it.web.form.manager.all", "sdcct.test.it.web.form.manager.ws.rfd.all", "sdcct.test.it.web.form.manager.ws.rfd.client" })
public class RfdFormManagerWebServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
    @Resource(name = "formTest1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Form testForm1;

    @Resource(name = "clientFormManagerRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private JaxWsClient testClient;

    @Test(enabled = false)
    public void testRetrieveForm() throws Exception {
        this.testClient.invoke(this.testClient.buildInvocationDelegate(), RfdWsXmlQnames.RETRIEVE_FORM_OP,
            new RetrieveFormRequestTypeImpl().setWorkflowData(new WorkflowDataTypeImpl().setFormID(this.testForm1.getId()).setEncodedResponse(true)));
    }
}
