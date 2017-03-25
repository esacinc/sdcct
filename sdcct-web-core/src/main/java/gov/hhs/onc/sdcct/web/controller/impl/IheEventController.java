package gov.hhs.onc.sdcct.web.controller.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.controller.JsonPostRequestMapping;
import gov.hhs.onc.sdcct.web.controller.PathNames;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.collections.map.LRUMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static gov.hhs.onc.sdcct.web.controller.PathNames.FORM_ARCHIVER_EVENT;
import static gov.hhs.onc.sdcct.web.controller.PathNames.FORM_MANAGER_EVENT;
import static gov.hhs.onc.sdcct.web.controller.PathNames.FORM_RECEIVER_EVENT;

@RestController("controllerIheEvent")
@RequestMapping(SdcctStringUtils.SLASH + PathNames.IHE)
public class IheEventController<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>> {
    private final static int NUM_RECENT_TESTCASE_EVENTS = 25;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private final Map<Long, V> iheTestcaseEvents = Collections.synchronizedMap(new LRUMap(NUM_RECENT_TESTCASE_EVENTS));

    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH + PathNames.EVENT + SdcctStringUtils.SLASH + PathNames.POLL })
    public ResponseEntity<?> poll(@RequestParam("lastSeenTxId") long txId) {
        if (!this.iheTestcaseEvents.isEmpty()) {
            return ResponseEntity.ok(this.iheTestcaseEvents.values().stream().filter(result -> result.getTxId() > txId).collect(Collectors.toList()));
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
        this.iheTestcaseEvents.put(testcaseResult.getTxId(), testcaseResult);
    }
}
