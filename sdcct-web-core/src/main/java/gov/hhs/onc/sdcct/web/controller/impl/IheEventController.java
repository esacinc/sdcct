package gov.hhs.onc.sdcct.web.controller.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static gov.hhs.onc.sdcct.web.controller.PathNames.FORM_ARCHIVER_EVENT;
import static gov.hhs.onc.sdcct.web.controller.PathNames.FORM_MANAGER_EVENT;
import static gov.hhs.onc.sdcct.web.controller.PathNames.FORM_RECEIVER_EVENT;

@RestController("controllerIheEvent")
@RequestMapping(SdcctStringUtils.SLASH + PathNames.IHE)
public class IheEventController<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>> {
    private final Map<UUID, V> iheTestcaseEvents = new ConcurrentHashMap<>();

    @Resource(name = "txIdGenIheTestcaseEvent")
    private TxIdGenerator txIdGen;

    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH + PathNames.EVENT + SdcctStringUtils.SLASH + PathNames.POLL })
    public ResponseEntity<?> poll() {
        if (!this.iheTestcaseEvents.isEmpty()) {
            // noinspection ConstantConditions
            Entry<UUID, V> entrySet = this.iheTestcaseEvents.entrySet().stream().findFirst().get();

            this.iheTestcaseEvents.remove(entrySet.getKey());

            return ResponseEntity.ok(entrySet.getValue());
        }

        return ResponseEntity.noContent().build();
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + FORM_ARCHIVER_EVENT })
    public void sendIheFormArchiverTestcaseResult(@Valid @RequestBody IheFormArchiverTestcaseResult iheFormArchiverTestcaseResult) {
        sendResult((V) iheFormArchiverTestcaseResult);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + FORM_MANAGER_EVENT })
    public void sendIheFormManagerTestcaseResult(@Valid @RequestBody IheFormManagerTestcaseResult iheFormManagerTestcaseResult) {
        sendResult((V) iheFormManagerTestcaseResult);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @JsonPostRequestMapping(value = { SdcctStringUtils.SLASH + FORM_RECEIVER_EVENT })
    public void sendIheFormReceiverTestcaseResult(@Valid @RequestBody IheFormReceiverTestcaseResult iheFormReceiverTestcaseResult) {
        sendResult((V) iheFormReceiverTestcaseResult);
    }

    private void sendResult(V testcaseResult) {
        this.iheTestcaseEvents.put(this.txIdGen.generateId(), testcaseResult);
    }
}
