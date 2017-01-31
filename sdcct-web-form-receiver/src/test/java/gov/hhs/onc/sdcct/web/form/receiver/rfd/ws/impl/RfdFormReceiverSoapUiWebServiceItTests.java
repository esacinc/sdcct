package gov.hhs.onc.sdcct.web.form.receiver.rfd.ws.impl;

import gov.hhs.onc.sdcct.web.test.soapui.SoapUiTest;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebItTests;
import org.testng.annotations.Test;

@SoapUiTest(projectFile = "${sdcct.soapui.dir}/soapui-sdcct-form-receiver-rfd-project.xml")
@Test(groups = { "sdcct.test.it.web.form.receiver.all", "sdcct.test.it.web.form.receiver.rfd.all", "sdcct.test.it.web.form.receiver.rfd.ws.all",
    "sdcct.test.it.web.form.receiver.rfd.ws.soapui" })
public class RfdFormReceiverSoapUiWebServiceItTests extends AbstractSoapUiWebItTests {
    @Override
    @Test
    public void testTestCase() throws Exception {
        super.testTestCase();
    }
}
