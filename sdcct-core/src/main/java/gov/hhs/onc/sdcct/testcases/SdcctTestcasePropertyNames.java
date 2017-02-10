package gov.hhs.onc.sdcct.testcases;

import gov.hhs.onc.sdcct.rfd.ws.RfdWsResponseType;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;

public final class SdcctTestcasePropertyNames {
    public final static String IHE_FORM_MANAGER_TESTCASE_RESULT = IheFormManagerTestcaseResult.class.getName();
    public final static String IHE_FORM_MANAGER_TESTCASE_SUBMISSION = IheFormManagerTestcaseSubmission.class.getName();

    public final static String RFD_CONTENT = "content";
    public final static String RFD_CONTENT_TYPE = "contentType";
    public final static String RFD_ENCODED_RESPONSE = "encodedResponse";
    public final static String RFD_ENCODED_RESPONSE_RESPONSE_CONTENT_TYPE = RFD_ENCODED_RESPONSE + " responseContentType";
    public final static String RFD_FORM = "form";
    public final static String RFD_FORM_ID = "formID";
    public final static String RFD_RESPONSE_CODE = "responseCode";
    public final static String RFD_SDC_SUBMISSION_PACKAGE = "SDC Submission Package";
    public final static String RFD_SOAP_FAULT = "SOAP Fault";
    public final static String RFD_SOAP_FAULT_REASON_TEXT = "Reason Text";
    public final static String RFD_URL = RfdWsResponseType.URL.getId();

    private SdcctTestcasePropertyNames() {
    }
}
