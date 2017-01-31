package gov.hhs.onc.sdcct.web.form.receiver.fhir.ws.impl;

import gov.hhs.onc.sdcct.web.test.soapui.SoapUiTest;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebItTests;
import org.testng.annotations.Test;

@SoapUiTest(projectFile = "${sdcct.soapui.dir}/soapui-sdcct-form-receiver-fhir-project.xml")
@Test(groups = { "sdcct.test.it.web.form.receiver.all", "sdcct.test.it.web.form.receiver.fhir.all", "sdcct.test.it.web.form.receiver.fhir.ws.all",
    "sdcct.test.it.web.form.receiver.fhir.ws.soapui" })
public class FhirFormReceiverSoapUiWebServiceItTests extends AbstractSoapUiWebItTests {
    @Override
    @Test
    public void testTestCase() throws Exception {
        super.testTestCase();
    }
}
