package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.fhir.FhirWsPathParamNames;
import gov.hhs.onc.sdcct.fhir.FhirWsQueryParamNames;
import gov.hhs.onc.sdcct.fhir.FhirWsResourceNames;
import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.net.utils.SdcctUriUtils;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.form.manager.all", "sdcct.test.it.web.form.manager.ws.fhir" })
public class FhirFormManagerWebServiceIntegrationTests extends AbstractSdcctWebIntegrationTests {
    @Resource(name = "formTest1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Form testForm1;

    @Resource(name = "clientFormManagerFhirLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Client testClient;

    @Test
    public void testReadQuestionnaire() throws Exception {
        String testFormId1 = this.testForm1.getId();
        WebClient testWebClient =
            WebClient.fromClient(this.testClient).path(
                (SdcctUriUtils.PATH_DELIM + FhirWsResourceNames.QUESTIONNAIRE + SdcctUriUtils.PATH_DELIM + SdcctUriUtils.PATH_PARAM_PREFIX
                    + FhirWsPathParamNames.ID + SdcctUriUtils.PATH_PARAM_SUFFIX), testFormId1).query(FhirWsQueryParamNames.PRETTY, true);
        URI testUri1 = testWebClient.getCurrentURI();
        FhirFormatType[] testFormatTypes = FhirFormatType.values();
        Map<FhirFormatType, Response> testRespMap = new EnumMap<>(FhirFormatType.class);

        for (FhirFormatType testFormatType : testFormatTypes) {
            testRespMap.put(testFormatType, testWebClient.replaceHeader(HttpHeaders.ACCEPT, testFormatType.getMediaType().toString()).get());
        }

        Response testResp;
        int testRespStatusCode;
        HttpStatus testRespStatus;
        String testRespStatusText;
        byte[] testRespEntityContent;

        for (FhirFormatType testFormatType : testFormatTypes) {
            Assert.assertSame((testRespStatus = HttpStatus.valueOf((testRespStatusCode = (testResp = testRespMap.get(testFormatType)).getStatus()))),
                HttpStatus.OK, String.format("Invalid FHIR SDC Questionnaire read test query (uri=%s, formatType=%s) response (statusCode=%d, statusText=%s).",
                    testUri1, testFormatType.name(), testRespStatusCode, (testRespStatusText = testRespStatus.getReasonPhrase())));

            Assert.assertTrue(!ArrayUtils.isEmpty((testRespEntityContent = testResp.readEntity(byte[].class))), String.format(
                "Invalid FHIR SDC Questionnaire read test query (uri=%s, formatType=%s) response (statusCode=%d statusText=%s) entity content:\n%s", testUri1,
                testFormatType.name(), testRespStatusCode, testRespStatusText, new String(testRespEntityContent, StandardCharsets.UTF_8)));
        }
    }
}
