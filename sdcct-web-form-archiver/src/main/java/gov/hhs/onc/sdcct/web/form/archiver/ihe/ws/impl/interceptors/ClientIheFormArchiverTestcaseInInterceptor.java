package gov.hhs.onc.sdcct.web.form.archiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.rfd.ArchiveFormResponseType;
import gov.hhs.onc.sdcct.rfd.impl.ArchiveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractClientIheTestcaseInInterceptor;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormArchiverTestcaseIn")
public class ClientIheFormArchiverTestcaseInInterceptor extends
    AbstractClientIheTestcaseInInterceptor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult, ArchiveFormResponseType> {
    public ClientIheFormArchiverTestcaseInInterceptor() {
        super(ArchiveFormResponseType.class, ArchiveFormResponseTypeImpl.class, IheFormArchiverTestcaseResult.class,
            SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT, SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_SUBMISSION,
            RfdWsXmlNames.ARCHIVE_FORM_RESP);
    }

    @Override
    protected void validateResponseInternal(IheFormArchiverTestcase testcase, IheFormArchiverTestcaseSubmission submission,
        IheFormArchiverTestcaseResult result, ArchiveFormResponseType response) {
    }
}
