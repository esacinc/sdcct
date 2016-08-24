package gov.hhs.onc.sdcct.web.form.receiver.fhir.ws.impl;

import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebServiceIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.form.receiver.all", "sdcct.test.it.web.form.receiver.fhir.all", "sdcct.test.it.web.form.receiver.fhir.ws.all",
    "sdcct.test.it.web.form.receiver.fhir.ws.soapui" })
public class FhirFormReceiverSoapUiWebServiceIntegrationTests extends AbstractSoapUiWebServiceIntegrationTests {
    @Override
    @Test
    public void testTestCase() throws Exception {
        super.testTestCase();
    }

    @Autowired
    @Override
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    protected void
        setProjectSource(@Value("classpath*:${sdcct.data.soapui.dir.path}/soapui-sdcct-form-receiver-fhir-project.xml") ResourceSource projectSrc) {
        super.setProjectSource(projectSrc);
    }
}
