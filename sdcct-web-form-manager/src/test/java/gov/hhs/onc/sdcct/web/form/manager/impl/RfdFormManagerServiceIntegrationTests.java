package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.rfd.RfdXmlQnames;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormRequestTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.WorkflowDataTypeImpl;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import javax.annotation.Resource;
import org.apache.cxf.endpoint.Client;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.form.manager.all", "sdcct.test.it.web.form.manager.ws.rfd" })
public class RfdFormManagerServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
    @Resource(name = "formTest1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Form testForm1;

    @Resource(name = "clientFormManagerRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Client testClient;

    @Test
    public void testRetrieveForm() throws Exception {
        this.testClient.invoke(RfdXmlQnames.RETRIEVE_FORM_OP,
            new RetrieveFormRequestTypeImpl(null, new WorkflowDataTypeImpl(this.testForm1.getId(), true, null, null, null)));
    }
}
