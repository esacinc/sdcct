package gov.hhs.onc.sdcct.web.form.manager.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsResponseType;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.sdc.SdcRetrieveFormPackageType;
import gov.hhs.onc.sdcct.sdc.XmlPackage;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractClientIheTestcaseInInterceptor;
import javax.xml.bind.JAXBElement;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormManagerTestcaseIn")
public class ClientIheFormManagerTestcaseInInterceptor extends
    AbstractClientIheTestcaseInInterceptor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult, RetrieveFormResponseType> {
    public ClientIheFormManagerTestcaseInInterceptor() {
        super(RetrieveFormResponseType.class, RetrieveFormResponseTypeImpl.class, IheFormManagerTestcaseResult.class,
            SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT, SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_SUBMISSION,
            RfdWsXmlNames.RETRIEVE_FORM_RESP);
    }

    @Override
    protected void validateResponseInternal(IheFormManagerTestcase testcase, IheFormManagerTestcaseSubmission submission, IheFormManagerTestcaseResult result,
        RetrieveFormResponseType actualResponse) {
        RetrieveFormResponseType expectedResponse = testcase.getResponse();
        // noinspection ConstantConditions
        String expectedContentType = expectedResponse.getContentType();
        String testcaseId = testcase.getId();

        if (!actualResponse.getContentType().equals(expectedContentType)) {
            result.getMessages().get(SdcctIssueSeverity.ERROR).add(String.format("%s contains unexpected contentType (expected=%s, actual=%s).",
                this.wsResponseName, expectedContentType, actualResponse.getContentType()));
        }

        if (!actualResponse.getForm().hasContent()) {
            result.getMessages().get(SdcctIssueSeverity.ERROR).add(String.format("%s form does not contain content.", this.wsResponseName));
        } else {
            Object content = actualResponse.getForm().getContent();

            // noinspection ConstantConditions
            if (expectedContentType.equals(RfdWsResponseType.XML.getMediaType().toString())) {
                if (content instanceof AnyXmlContentType) {
                    SdcRetrieveFormPackageType packageType =
                        (SdcRetrieveFormPackageType) ((JAXBElement) ((AnyXmlContentType) content).getAny().get(0)).getValue();
                    String expectedFormId = submission.getFormId();
                    String actualFormId = ((XmlPackage) packageType.getContent().get(0)).getFormDesign().getId();

                    if (expectedFormId != null && !expectedFormId.equals(actualFormId)) {
                        result.getMessages().get(SdcctIssueSeverity.ERROR).add(String.format(
                            "Form ID (%s) in %s does not equal form ID (%s) requested in submission.", actualFormId, this.wsResponseName, expectedFormId));
                    }
                } else {
                    result.getMessages().get(SdcctIssueSeverity.ERROR)
                        .add(String.format("%s for testcase (id=%s) does not contain the Structured element.", this.wsResponseName, testcaseId));
                }
            } else // noinspection ConstantConditions
            if (expectedContentType.equals(RfdWsResponseType.HTML.getMediaType().toString())) {
                if (!(content instanceof AnyXmlContentType)) {
                    result.getMessages().get(SdcctIssueSeverity.ERROR)
                        .add(String.format("%s for testcase (id=%s) does not contain the Structured element.", this.wsResponseName, testcaseId));
                }
            } else if (expectedContentType.equals(RfdWsResponseType.URL.getId())) {
                if (!(content instanceof String)) {
                    result.getMessages().get(SdcctIssueSeverity.ERROR)
                        .add(String.format("%s for testcase (id=%s) does not contain the URL element.", this.wsResponseName, testcaseId));
                } else {
                    String expectedFormId = submission.getFormId();
                    String actualFormId = (String) content;

                    if (expectedFormId != null && !expectedFormId.equals(actualFormId)) {
                        result.getMessages().get(SdcctIssueSeverity.ERROR).add(String.format(
                            "Form ID (%s) in %s does not equal form ID (%s) requested in submission.", actualFormId, this.wsResponseName, expectedFormId));
                    }
                }
            } else {
                result.getMessages().get(SdcctIssueSeverity.ERROR)
                    .add(String.format("%s for testcase (id=%s) does not contain a valid contentType.", this.wsResponseName, testcaseId));
            }
        }
    }
}
