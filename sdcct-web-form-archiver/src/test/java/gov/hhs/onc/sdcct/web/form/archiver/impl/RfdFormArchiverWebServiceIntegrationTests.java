package gov.hhs.onc.sdcct.web.form.archiver.impl;

import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.form.archiver.all", "sdcct.test.it.web.form.archiver.ws.rfd" })
public class RfdFormArchiverWebServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
    // @formatter:off
    /*
    @Resource(name = "formTest1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Form testForm1;

    @Resource(name = "clientFormArchiverRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private JaxWsClient testClient;

    @Test(enabled = false)
    public void testArchiveForm() throws Exception {
        this.testClient.invoke(this.testClient.buildInvocationDelegate(), RfdWsXmlQnames.ARCHIVE_FORM_OP,
            new AnyXMLContentTypeImpl(Collections.singletonList(this.testForm1.getPackageElement())));
    }
    */
    // @formatter:on
}
