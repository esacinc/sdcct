package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.FhirWsPathParamNames;
import gov.hhs.onc.sdcct.fhir.FhirWsResourceNames;
import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.io.SdcctMediaTypes;
import gov.hhs.onc.sdcct.net.utils.SdcctUriUtils;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import java.nio.charset.StandardCharsets;
import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.form.manager.all", "sdcct.test.it.web.form.manager.ws.fhir" })
public class FhirFormManagerWebServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(FhirFormManagerWebServiceIntegrationTests.class);

    @Resource(name = "formTest1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Form testForm1;

    @Resource(name = "clientFormManagerFhirLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Client testClient;

    @Test
    public void testReadQuestionnaire() throws Exception {
        String testFormId1 = this.testForm1.getId(), testUri1 = this.testClient.getCurrentURI().toString();
        Response testResp =
            WebClient
                .fromClient(this.testClient)
                .path(
                    (SdcctUriUtils.PATH_DELIM + FhirWsResourceNames.QUESTIONNAIRE + SdcctUriUtils.PATH_DELIM + SdcctUriUtils.PATH_PARAM_PREFIX
                        + FhirWsPathParamNames.ID + SdcctUriUtils.PATH_PARAM_SUFFIX), testFormId1).accept(SdcctMediaTypes.APP_JSON_FHIR_VALUE).get();

        LOGGER.info(String.format("FHIR SDC Questionnaire (id=%s) read from URI (%s):\n%s", testFormId1, testUri1, new String(
            testResp.readEntity(byte[].class), StandardCharsets.UTF_8)));
    }
}
