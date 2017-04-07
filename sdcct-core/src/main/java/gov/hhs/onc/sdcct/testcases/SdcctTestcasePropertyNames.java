package gov.hhs.onc.sdcct.testcases;

import gov.hhs.onc.sdcct.rfd.ws.RfdWsResponseType;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormReceiverTestcaseSubmission;

public final class SdcctTestcasePropertyNames {
    public final static String IHE_FORM_ARCHIVER_TESTCASE_RESULT = IheFormArchiverTestcaseResult.class.getName();
    public final static String IHE_FORM_MANAGER_TESTCASE_RESULT = IheFormManagerTestcaseResult.class.getName();
    public final static String IHE_FORM_RECEIVER_TESTCASE_RESULT = IheFormReceiverTestcaseResult.class.getName();

    public final static String IHE_FORM_ARCHIVER_TESTCASE_SUBMISSION = IheFormArchiverTestcaseSubmission.class.getName();
    public final static String IHE_FORM_MANAGER_TESTCASE_SUBMISSION = IheFormManagerTestcaseSubmission.class.getName();
    public final static String IHE_FORM_RECEIVER_TESTCASE_SUBMISSION = IheFormReceiverTestcaseSubmission.class.getName();

    public final static String RFD_ANY_XML_CONTENT_TYPE = "anyXMLContentType";
    public final static String RFD_CONTENT = "content";
    public final static String RFD_CONTENT_TYPE = "contentType";
    public final static String RFD_ENCODED_RESPONSE = "encodedResponse";
    public final static String RFD_ENCODED_RESPONSE_RESPONSE_CONTENT_TYPE = RFD_ENCODED_RESPONSE + " responseContentType";
    public final static String RFD_FORM = "form";
    public final static String RFD_FORM_ID = "formID";
    public final static String RFD_RESPONSE_CODE = "responseCode";
    public final static String RFD_SDC_SUBMISSION_PACKAGE = "SDC Submission Package";
    public final static String RFD_SOAP_FAULT = "SOAP Fault";
    public final static String RFD_SOAP_FAULT_REASON_TEXT = RFD_SOAP_FAULT + " Reason Text";
    public final static String RFD_URL = RfdWsResponseType.URL.getId();

    private SdcctTestcasePropertyNames() {
    }
}
