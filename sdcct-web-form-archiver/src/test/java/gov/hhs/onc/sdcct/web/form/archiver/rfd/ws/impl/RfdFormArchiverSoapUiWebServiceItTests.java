package gov.hhs.onc.sdcct.web.form.archiver.rfd.ws.impl;

import gov.hhs.onc.sdcct.web.test.soapui.SoapUiTest;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebItTests;
import org.testng.annotations.Test;

@SoapUiTest(projectFile = "${sdcct.soapui.dir}/soapui-sdcct-form-archiver-rfd-project.xml")
@Test(groups = { "sdcct.test.it.web.form.archiver.all", "sdcct.test.it.web.form.archiver.rfd.all", "sdcct.test.it.web.form.archiver.rfd.ws.all",
    "sdcct.test.it.web.form.archiver.rfd.ws.soapui" })
public class RfdFormArchiverSoapUiWebServiceItTests extends AbstractSoapUiWebItTests {
    @Override
    @Test
    public void testTestCase() throws Exception {
        super.testTestCase();
    }
}
