package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.controller.JsonPostRequestMapping;
import gov.hhs.onc.sdcct.web.controller.PathNames;
import gov.hhs.onc.sdcct.web.form.archiver.ihe.IheFormArchiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.manager.ihe.IheFormManagerTestcaseProcessor;
import gov.hhs.onc.sdcct.web.form.receiver.ihe.IheFormReceiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.gateway.controller.InvalidRequestException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("controllerIhe")
@RequestMapping(SdcctStringUtils.SLASH + PathNames.IHE)
public class IheController {
    @Autowired
    private IheFormArchiverTestcaseProcessor iheFormArchiverTestcaseProc;

    @Autowired
    private IheFormManagerTestcaseProcessor iheFormManagerTestcaseProc;

    @Autowired
    private IheFormReceiverTestcaseProcessor iheFormReceiverTestcaseProc;

    private final static String INVALID_REQUEST = "Invalid request";

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + PathNames.FORM_ARCHIVER_PROCESS })
    public ResponseEntity<?> processIheFormArchiverTestcase(@Valid @RequestBody IheFormArchiverTestcaseSubmission iheFormArchiverTestcaseSubmission,
        BindingResult bindingResult) throws Exception {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormArchiverTestcaseProc.process(iheFormArchiverTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + PathNames.FORM_MANAGER_PROCESS })
    public ResponseEntity<?> processIheFormManagerTestcase(@Valid @RequestBody IheFormManagerTestcaseSubmission iheFormManagerTestcaseSubmission,
        BindingResult bindingResult) throws Exception {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormManagerTestcaseProc.process(iheFormManagerTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + PathNames.FORM_RECEIVER_PROCESS })
    public ResponseEntity<?> processIheFormReceiverTestcase(@Valid @RequestBody IheFormReceiverTestcaseSubmission iheFormReceiverTestcaseSubmission,
        BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok().body(this.iheFormReceiverTestcaseProc.process(iheFormReceiverTestcaseSubmission));
        } else {
            throw new InvalidRequestException(INVALID_REQUEST, null, bindingResult);
        }
    }
}
