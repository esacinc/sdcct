package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.web.test.soapui.impl.AbstractSoapUiWebServiceIntegrationTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.form.manager.all", "sdcct.test.it.web.form.manager.ws.fhir.all", "sdcct.test.it.web.form.manager.ws.fhir.soapui" })
public class FhirFormManagerSoapUiWebServiceIntegrationTests extends AbstractSoapUiWebServiceIntegrationTests {
    @Override
    @Test
    public void testTestCase() throws Exception {
        super.testTestCase();
    }

    @Autowired
    @Override
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    protected void
        setProjectSource(@Value("classpath*:${sdcct.data.soapui.src.dir.path}/soapui-sdcct-form-manager-fhir-project.xml") ResourceSource projectSrc) {
        super.setProjectSource(projectSrc);
    }
}
