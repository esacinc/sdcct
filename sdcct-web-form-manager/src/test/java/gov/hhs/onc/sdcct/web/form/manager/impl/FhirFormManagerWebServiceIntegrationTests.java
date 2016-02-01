package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.fhir.FhirWsPathParamNames;
import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.web.form.manager.FhirFormManagerWebService;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctWebIntegrationTests;
import gov.hhs.onc.sdcct.ws.impl.JaxRsClient;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.impl.UriBuilderImpl;
import org.apache.cxf.jaxrs.model.wadl.WadlGenerator;
import org.springframework.http.HttpMethod;
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
    private JaxRsClient testClient;

    @Test(dependsOnMethods = { "testGenerateWadl" })
    public void testReadQuestionnaire() throws Exception {
        String testFormId1 = this.testForm1.getId(), testUri1 =
            new UriBuilderImpl(this.testClient.getDelegate().getCurrentURI()).path(FhirFormManagerWebService.class.getMethod("readQuestionnaire", String.class))
                .resolveTemplate(FhirWsPathParamNames.ID, testFormId1).build().toString();
        FhirFormatType[] testFormatTypes = FhirFormatType.values();
        Map<FhirFormatType, Response> testRespMap = new EnumMap<>(FhirFormatType.class);

        for (FhirFormatType testFormatType : testFormatTypes) {
            testRespMap.put(testFormatType,
                this.testClient.invoke(
                    this.testClient.buildInvocationDelegate().to(testUri1, false).replaceHeader(HttpHeaders.ACCEPT, testFormatType.getMediaType().toString()),
                    HttpMethod.GET));
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

            Assert.assertTrue(!ArrayUtils.isEmpty((testRespEntityContent = testResp.readEntity(byte[].class))),
                String.format(
                    "Invalid FHIR SDC Questionnaire read test query (uri=%s, formatType=%s) response (statusCode=%d statusText=%s) entity content:\n%s",
                    testUri1, testFormatType.name(), testRespStatusCode, testRespStatusText, new String(testRespEntityContent, StandardCharsets.UTF_8)));
        }
    }

    @Test
    public void testGenerateWadl() throws Exception {
        URI testWadlUri = new UriBuilderImpl(this.testClient.getDelegate().getCurrentURI()).queryParam(WadlGenerator.WADL_QUERY).build();

        Assert.assertTrue(!StringUtils.isEmpty(IOUtils.toString(testWadlUri, StandardCharsets.UTF_8)),
            String.format("Unable to generate FHIR SDC Form Manager WADL via test query (uri=%s).", testWadlUri));
    }
}
