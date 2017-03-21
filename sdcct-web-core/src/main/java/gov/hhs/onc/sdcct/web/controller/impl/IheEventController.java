package gov.hhs.onc.sdcct.web.controller.impl;

import static gov.hhs.onc.sdcct.web.controller.PathNames.*;

import gov.hhs.onc.sdcct.logging.impl.TxIdGenerator;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.controller.JsonPostRequestMapping;
import gov.hhs.onc.sdcct.web.controller.PathNames;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController("controllerIheEvent")
@RequestMapping(SdcctStringUtils.SLASH + PathNames.IHE)
public class IheEventController {
    private final Map<UUID, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Resource(name = "txIdGenServerSentEvent")
    private TxIdGenerator txIdGen;

    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH + PathNames.EVENT + SdcctStringUtils.SLASH + PathNames.STREAM })
    public ResponseEntity<SseEmitter> stream() {
        SseEmitter sseEmitter = new SseEmitter();
        UUID uuid = this.txIdGen.generateId();

        this.sseEmitters.put(uuid, sseEmitter);

        sseEmitter.onCompletion(() -> this.sseEmitters.remove(uuid));

        return ResponseEntity.ok(sseEmitter);
    }

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + FORM_ARCHIVER_EVENT })
    public void sendIheFormArchiverTestcaseResult(@Valid @RequestBody IheFormArchiverTestcaseResult iheFormArchiverTestcaseResult) {
        processResult(iheFormArchiverTestcaseResult);
    }

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + FORM_MANAGER_EVENT })
    public void sendIheFormManagerTestcaseResult(@Valid @RequestBody IheFormManagerTestcaseResult iheFormManagerTestcaseResult) {
        processResult(iheFormManagerTestcaseResult);
    }

    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + FORM_RECEIVER_EVENT })
    public void sendIheFormReceiverTestcaseResult(@Valid @RequestBody IheFormReceiverTestcaseResult iheFormReceiverTestcaseResult) {
        processResult(iheFormReceiverTestcaseResult);
    }

    private <T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>> void processResult(V testcaseResult) {
        this.sseEmitters.forEach((UUID uuid, SseEmitter sseEmitter) -> {
            try {
                sseEmitter.send(testcaseResult, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
                this.sseEmitters.remove(uuid);
            }
        });
    }
}
