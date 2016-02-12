package gov.hhs.onc.sdcct.web.form.archiver;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.rfd.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.impl.AnyXMLContentTypeImpl;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Collections;

@Test(groups = { "sdcct.test.it.web.form.archiver.all", "sdcct.test.it.web.form.archiver.ws.rfd" })
public class RfdFormArchiverWebServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
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
}
