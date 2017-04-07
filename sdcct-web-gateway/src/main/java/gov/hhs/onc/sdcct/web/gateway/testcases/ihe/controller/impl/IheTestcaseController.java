package gov.hhs.onc.sdcct.web.gateway.testcases.ihe.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseProcessingException;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheTestcaseResultAddedEvent;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.web.form.archiver.testcases.ihe.IheFormArchiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.manager.testcases.ihe.IheFormManagerTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.receiver.testcases.ihe.IheFormReceiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.net.SdcctWebUris;
import gov.hhs.onc.sdcct.web.testcases.ihe.websocket.impl.IheTestcaseResultMessageImpl;
import gov.hhs.onc.sdcct.web.websocket.SdcctWebSocketPaths;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("controllerTestcaseIhe")
public class IheTestcaseController implements InitializingBean {
    @Autowired
    private IheFormArchiverTestcaseProcessor formArchiverTestcaseProc;

    @Autowired
    private IheFormManagerTestcaseProcessor formManagerTestcaseProc;

    @Autowired
    private IheFormReceiverTestcaseProcessor formReceiverTestcaseProc;

    @Autowired
    private IheTestcaseResultHandler testcaseResultHandler;

    @Autowired
    private SimpMessagingTemplate testcaseResultsMessagingTemplate;

    @Resource(name = "objMapperTestcases")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ObjectMapper objMapper;

    private TokenBuffer emptyJsonObjTokenBuffer;

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE },
        value = { SdcctWebUris.TESTCASES_IHE_FORM_ARCHIVER_PROCESS_PATH })
    public ResponseEntity<?> processFormArchiverTestcase(@RequestBody @Validated IheFormArchiverTestcaseSubmission testcaseSubmission,
        BindingResult testcaseSubmissionBindingResult) throws Exception {
        return this.processTestcase(this.formArchiverTestcaseProc, testcaseSubmission, testcaseSubmissionBindingResult);
    }

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE },
        value = { SdcctWebUris.TESTCASES_IHE_FORM_MANAGER_PROCESS_PATH })
    public ResponseEntity<?> processFormManagerTestcase(@RequestBody @Validated IheFormManagerTestcaseSubmission testcaseSubmission,
        BindingResult testcaseSubmissionBindingResult) throws Exception {
        return this.processTestcase(this.formManagerTestcaseProc, testcaseSubmission, testcaseSubmissionBindingResult);
    }

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE },
        value = { SdcctWebUris.TESTCASES_IHE_FORM_RECEIVER_PROCESS_PATH })
    public ResponseEntity<?> processFormReceiverTestcase(@RequestBody @Validated IheFormReceiverTestcaseSubmission testcaseSubmission,
        BindingResult testcaseSubmissionBindingResult) throws Exception {
        return this.processTestcase(this.formReceiverTestcaseProc, testcaseSubmission, testcaseSubmissionBindingResult);
    }

    @EventListener
    public void handleTestcaseResultAdded(IheTestcaseResultAddedEvent testcaseResultAddedEvent) {
        this.testcaseResultsMessagingTemplate.convertAndSend(SdcctWebSocketPaths.TOPIC_TESTCASES_IHE_RESULTS,
            new IheTestcaseResultMessageImpl(testcaseResultAddedEvent.getResult()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        (this.emptyJsonObjTokenBuffer = new TokenBuffer(this.objMapper, false)).writeStartObject();
        this.emptyJsonObjTokenBuffer.writeEndObject();
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private <T extends IheTestcaseSubmission<?>, U extends IheTestcaseProcessor<?, T, ?>> ResponseEntity<?> processTestcase(U testcaseProc,
        T testcaseSubmission, BindingResult testcaseSubmissionBindingResult) throws Exception {
        if (!testcaseSubmissionBindingResult.hasErrors()) {
            try {
                this.testcaseResultHandler.addResult(testcaseProc.process(testcaseSubmission));
            } catch (Exception e) {
                IheTestcase testcase = testcaseSubmission.getTestcase();

                throw new SdcctTestcaseProcessingException(
                    String.format("Unable to process testcase (id=%s, name=%s) submission (txId=%s, submittedTimestamp=%s, endpointAddr=%s, formId=%s).",
                        ((testcase != null) ? testcase.getId() : null), ((testcase != null) ? testcase.getName() : null), testcaseSubmission.getTxId(),
                        testcaseSubmission.getSubmittedTimestamp(), testcaseSubmission.getEndpointAddress(), testcaseSubmission.getFormId()),
                    e).setBindingResult(testcaseSubmissionBindingResult);
            }
        } else {
            throw new SdcctTestcaseProcessingException(
                String.format("Testcase submission binding has %d error(s).", testcaseSubmissionBindingResult.getErrorCount()))
                    .setBindingResult(testcaseSubmissionBindingResult);
        }

        return ResponseEntity.ok(this.emptyJsonObjTokenBuffer);
    }
}
