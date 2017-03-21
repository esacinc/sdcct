package gov.hhs.onc.sdcct.web.form.archiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsaActions;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseInInterceptor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormArchiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.impl.IheFormArchiverTestcaseSubmissionImpl;
import java.util.List;
import org.apache.cxf.binding.soap.SoapMessage;
import org.springframework.stereotype.Component;

@Component("interceptorServerIheFormArchiverTestcaseIn")
public class ServerIheFormArchiverTestcaseInInterceptor extends
    AbstractServerIheTestcaseInInterceptor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult, AnyXmlContentType> {
    public ServerIheFormArchiverTestcaseInInterceptor() {
        super(AnyXmlContentTypeImpl.class, RfdWsXmlQnames.ARCHIVE_FORM_REQ, SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT,
            IheFormArchiverTestcaseResult.class, IheFormArchiverTestcaseResultImpl.class, IheFormArchiverTestcaseSubmissionImpl.class,
            RfdWsaActions.ARCHIVE_FORM);
    }

    @Override
    protected IheFormArchiverTestcaseResult processRequest(AnyXmlContentType request, SoapMessage message) throws Exception {
        return this.createResult(null, message, null);
    }

    @Override
    protected IheFormArchiverTestcase findTestcase(AnyXmlContentType request, List<IheFormArchiverTestcase> testcases, String formId) {
        return null;
    }

    @Override
    protected void validateRequest(IheFormArchiverTestcase testcase, IheFormArchiverTestcaseResult testcaseResult, AnyXmlContentType request) {
    }
}
