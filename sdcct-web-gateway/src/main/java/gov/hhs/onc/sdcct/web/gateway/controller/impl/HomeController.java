package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.testcases.SpecificationRole;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.gateway.controller.SdcctModelAttributes;
import gov.hhs.onc.sdcct.web.gateway.controller.ViewNames;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("controllerHome")
public class HomeController implements InitializingBean {
    private final static String IHE_TESTCASE_ID_PREFIX = "IHE_";

    @Value("${sdcct.ws.form.manager.rfd.url}")
    private String rfdFormManagerEndpointAddr;

    @Value("${sdcct.ws.form.receiver.rfd.url}")
    private String rfdFormReceiverEndpointAddr;

    @Value("${sdcct.ws.form.archiver.rfd.url}")
    private String rfdFormArchiverEndpointAddr;

    @Value("${sdcct.testcases.form.archiver.ihe.process.url}")
    private String iheFormArchiverProcessAddr;

    @Value("${sdcct.testcases.form.manager.ihe.process.url}")
    private String iheFormManagerProcessAddr;

    @Value("${sdcct.testcases.form.receiver.ihe.process.url}")
    private String iheFormReceiverProcessAddr;

    @Value("${sdcct.testcases.ihe.results.poll.url}")
    private String iheTestcasesResultsPollUrl;

    @Autowired
    private List<IheTestcase> iheTestcases;

    private Map<SpecificationRole, List<IheTestcase>> iheTestcasesMap = new HashMap<>();
    private Map<SpecificationRole, String> iheEndpointAddressesMap = new HashMap<>();
    private Map<SpecificationRole, String> iheTestcasesProcessUrlsMap = new HashMap<>();

    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH, (SdcctStringUtils.SLASH + ViewNames.HOME) })
    public ModelAndView displayHome() throws Exception {
        return new ModelAndView(ViewNames.HOME);
    }

    @ModelAttribute(value = SdcctModelAttributes.IHE_TESTCASES_RESULTS_POLL_URL_NAME)
    private String getIheTestcasesResultsPollUrl() {
        return this.iheTestcasesResultsPollUrl;
    }

    @ModelAttribute(value = SdcctModelAttributes.IHE_TESTCASES_NAME)
    private Map<SpecificationRole, List<IheTestcase>> getIheTestcases() {
        return this.iheTestcasesMap;
    }

    @ModelAttribute(value = SdcctModelAttributes.IHE_TESTCASES_PROCESS_URLS_NAME)
    private Map<SpecificationRole, String> getIheTestcasesProcessUrlsMap() {
        return this.iheTestcasesProcessUrlsMap;
    }

    @ModelAttribute(value = SdcctModelAttributes.IHE_ENDPOINT_ADDRESSES_NAME)
    private Map<SpecificationRole, String> getIheEndpointAddresses() {
        return this.iheEndpointAddressesMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.iheTestcasesMap = this.iheTestcases.stream()
            .sorted(Comparator.comparingInt((IheTestcase iheTestcase) -> Integer.valueOf(iheTestcase.getId().substring(IHE_TESTCASE_ID_PREFIX.length()))))
            .collect(Collectors.groupingBy(IheTestcase::getRoleTested));

        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_ARCHIVER, this.rfdFormArchiverEndpointAddr);
        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_MANAGER, this.rfdFormManagerEndpointAddr);
        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_RECEIVER, this.rfdFormReceiverEndpointAddr);

        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_ARCHIVER, this.iheFormArchiverProcessAddr);
        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_MANAGER, this.iheFormManagerProcessAddr);
        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_RECEIVER, this.iheFormReceiverProcessAddr);
    }
}
