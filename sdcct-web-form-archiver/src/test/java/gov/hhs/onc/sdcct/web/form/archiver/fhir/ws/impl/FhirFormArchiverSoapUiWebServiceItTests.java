package gov.hhs.onc.sdcct.web.form.archiver.fhir.ws.impl;

import gov.hhs.onc.sdcct.web.test.soapui.SoapUiTest;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebItTests;
import org.testng.annotations.Test;

@SoapUiTest(projectFile = "${sdcct.soapui.dir}/soapui-sdcct-form-archiver-fhir-project.xml")
@Test(groups = { "sdcct.test.it.web.form.archiver.all", "sdcct.test.it.web.form.archiver.fhir.all", "sdcct.test.it.web.form.archiver.fhir.ws.all",
    "sdcct.test.it.web.form.archiver.fhir.ws.soapui" })
public class FhirFormArchiverSoapUiWebServiceItTests extends AbstractSoapUiWebItTests {
    @Override
    @Test
    public void testTestCase() throws Exception {
        super.testTestCase();
    }
}
