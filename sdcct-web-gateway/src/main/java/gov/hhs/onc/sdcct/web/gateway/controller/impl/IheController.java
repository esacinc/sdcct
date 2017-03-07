package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.form.archiver.IheFormArchiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.manager.IheFormManagerTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.receiver.IheFormReceiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.gateway.controller.InvalidRequestException;
import gov.hhs.onc.sdcct.web.gateway.controller.JsonPostRequestMapping;
import gov.hhs.onc.sdcct.web.gateway.controller.ViewNames;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("controllerIhe")
@RequestMapping(SdcctStringUtils.SLASH + ViewNames.IHE)
public class IheController {
    @Autowired
    private IheFormArchiverTestcaseProcessor iheFormArchiverTestcaseProc;

    @Autowired
    private IheFormManagerTestcaseProcessor iheFormManagerTestcaseProc;

    @Autowired
    private IheFormReceiverTestcaseProcessor iheFormReceiverTestcaseProc;

    private final static String INVALID_REQUEST = "Invalid request";

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + ViewNames.FORM_ARCHIVER_PROCESS })
    public ResponseEntity<?> processIheFormArchiverTestcase(@Valid @RequestBody IheFormArchiverTestcaseSubmission iheFormArchiverTestcaseSubmission,
        BindingResult bindingResult) throws Exception {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormArchiverTestcaseProc.process(iheFormArchiverTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + ViewNames.FORM_MANAGER_PROCESS })
    public ResponseEntity<?> processIheFormManagerTestcase(@Valid @RequestBody IheFormManagerTestcaseSubmission iheFormManagerTestcaseSubmission,
        BindingResult bindingResult) throws Exception {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormManagerTestcaseProc.process(iheFormManagerTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + ViewNames.FORM_RECEIVER_PROCESS })
    public ResponseEntity<?> processIheFormReceiverTestcase(@Valid @RequestBody IheFormReceiverTestcaseSubmission iheFormReceiverTestcaseSubmission,
        BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormReceiverTestcaseProc.process(iheFormReceiverTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }
}
