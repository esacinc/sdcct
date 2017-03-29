package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.controller.ControllerPaths;
import gov.hhs.onc.sdcct.web.form.archiver.ihe.IheFormArchiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.manager.ihe.IheFormManagerTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.receiver.ihe.IheFormReceiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.gateway.controller.InvalidRequestException;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(ControllerPaths.IHE)
@RestController("controllerIhe")
public class IheController {
    private final static String INVALID_REQUEST = "Invalid request";

    @Autowired
    private IheFormArchiverTestcaseProcessor iheFormArchiverTestcaseProc;

    @Autowired
    private IheFormManagerTestcaseProcessor iheFormManagerTestcaseProc;

    @Autowired
    private IheFormReceiverTestcaseProcessor iheFormReceiverTestcaseProc;

    @Autowired
    private IheTestcaseResultHandler iheTestcaseResultHandler;

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE },
        value = { ControllerPaths.RESULTS_POLL })
    public ResponseEntity<?> pollResults(@Nonnegative @RequestParam long submittedAfterTimestamp) {
        List<IheTestcaseResult<?, ?>> testcaseResults = this.iheTestcaseResultHandler.findResults(submittedAfterTimestamp);

        return (!testcaseResults.isEmpty() ? ResponseEntity.ok(testcaseResults) : ResponseEntity.noContent().build());
    }

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE },
        value = { ControllerPaths.FORM_ARCHIVER_PROCESS })
    public ResponseEntity<?> processIheFormArchiverTestcase(@Valid @RequestBody IheFormArchiverTestcaseSubmission iheFormArchiverTestcaseSubmission,
        BindingResult bindingResult) throws Exception {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormArchiverTestcaseProc.process(iheFormArchiverTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE },
        value = { ControllerPaths.FORM_MANAGER_PROCESS })
    public ResponseEntity<?> processIheFormManagerTestcase(@Valid @RequestBody IheFormManagerTestcaseSubmission iheFormManagerTestcaseSubmission,
        BindingResult bindingResult) throws Exception {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormManagerTestcaseProc.process(iheFormManagerTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }

    @RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_VALUE },
        value = { ControllerPaths.FORM_RECEIVER_PROCESS })
    public ResponseEntity<?> processIheFormReceiverTestcase(@Valid @RequestBody IheFormReceiverTestcaseSubmission iheFormReceiverTestcaseSubmission,
        BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormReceiverTestcaseProc.process(iheFormReceiverTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }
}
