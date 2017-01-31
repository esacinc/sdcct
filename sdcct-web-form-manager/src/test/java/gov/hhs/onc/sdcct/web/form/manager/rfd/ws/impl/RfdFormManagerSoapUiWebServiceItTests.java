package gov.hhs.onc.sdcct.web.form.manager.rfd.ws.impl;

import gov.hhs.onc.sdcct.web.test.soapui.SoapUiTest;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebItTests;
import org.testng.annotations.Test;

@SoapUiTest(projectFile = "${sdcct.soapui.dir}/soapui-sdcct-form-manager-rfd-project.xml")
@Test(groups = { "sdcct.test.it.web.form.manager.all", "sdcct.test.it.web.form.manager.rfd.all", "sdcct.test.it.web.form.manager.rfd.ws.all",
    "sdcct.test.it.web.form.manager.rfd.ws.soapui" })
public class RfdFormManagerSoapUiWebServiceItTests extends AbstractSoapUiWebItTests {
    @Override
    @Test
    public void testTestCase() throws Exception {
        super.testTestCase();
    }
}
