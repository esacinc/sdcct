package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.testcases.SpecificationRole;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.gateway.controller.SdcctModelAttributes;
import gov.hhs.onc.sdcct.web.gateway.controller.SdcctViewNames;
import gov.hhs.onc.sdcct.web.websocket.SdcctWebSocketPaths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("controllerHome")
public class HomeController implements InitializingBean {
    @Value("${sdcct.ws.form.archiver.rfd.url}")
    private String rfdFormArchiverEndpointAddr;

    @Value("${sdcct.ws.form.manager.rfd.url}")
    private String rfdFormManagerEndpointAddr;

    @Value("${sdcct.ws.form.receiver.rfd.url}")
    private String rfdFormReceiverEndpointAddr;

    @Value("${sdcct.testcases.ihe.form.archiver.process.url}")
    private String iheFormArchiverProcessUrl;

    @Value("${sdcct.testcases.ihe.form.manager.process.url}")
    private String iheFormManagerProcessUrl;

    @Value("${sdcct.testcases.ihe.form.receiver.process.url}")
    private String iheFormReceiverProcessUrl;

    @Value("${sdcct.testcases.ihe.results.websocket.url}")
    private String iheTestcaseResultsWebsocketUrl;
    
    @Autowired
    private List<IheTestcase> iheTestcases;

    private Map<SpecificationRole, List<IheTestcase>> iheTestcasesMap = new HashMap<>();
    private Map<SpecificationRole, String> iheEndpointAddressesMap = new HashMap<>();
    private Map<SpecificationRole, String> iheTestcasesProcessUrlsMap = new HashMap<>();

    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH, (SdcctStringUtils.SLASH + SdcctViewNames.HOME) })
    public String displayHome() throws Exception {
        return SdcctViewNames.HOME;
    }

    @ModelAttribute(SdcctModelAttributes.TESTCASES_IHE_NAME)
    private Map<SpecificationRole, List<IheTestcase>> getIheTestcases() {
        return this.iheTestcasesMap;
    }

    @ModelAttribute(SdcctModelAttributes.TESTCASES_IHE_ENDPOINT_ADDRESSES_NAME)
    private Map<SpecificationRole, String> getIheEndpointAddresses() {
        return this.iheEndpointAddressesMap;
    }
    
    @ModelAttribute(SdcctModelAttributes.TESTCASES_IHE_PROCESS_URLS_NAME)
    private Map<SpecificationRole, String> getIheTestcasesProcessUrlsMap() {
        return this.iheTestcasesProcessUrlsMap;
    }
    
    @ModelAttribute(SdcctModelAttributes.TESTCASES_IHE_RESULTS_TOPIC_WEBSOCKET_ENDPOINT_NAME)
    private String getIheTestcasesResultsTopicWebsocketEndpoint() {
        return SdcctWebSocketPaths.TOPIC_TESTCASES_IHE_RESULTS;
    }
    
    @ModelAttribute(SdcctModelAttributes.TESTCASES_IHE_RESULTS_WEBSOCKET_URL_NAME)
    private String getIheTestcasesResultsWebsocketUrl() {
        return this.iheTestcaseResultsWebsocketUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.iheTestcasesMap = this.iheTestcases.stream().sorted(OrderComparator.INSTANCE).collect(Collectors.groupingBy(IheTestcase::getRoleTested));

        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_ARCHIVER, this.rfdFormArchiverEndpointAddr);
        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_MANAGER, this.rfdFormManagerEndpointAddr);
        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_RECEIVER, this.rfdFormReceiverEndpointAddr);

        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_ARCHIVER, this.iheFormArchiverProcessUrl);
        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_MANAGER, this.iheFormManagerProcessUrl);
        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_RECEIVER, this.iheFormReceiverProcessUrl);
    }
}
