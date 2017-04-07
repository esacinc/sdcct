package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.testcases.SpecificationRole;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.utils.SdcctDateFormatUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.gateway.controller.SdcctModelAttributes;
import gov.hhs.onc.sdcct.web.gateway.controller.SdcctViewNames;
import gov.hhs.onc.sdcct.web.websocket.SdcctWebSocketPaths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

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

    @Value("${sdcct.build.module.sdcct-web-gateway.git.build.time}")
    private String gitModuleBuildTime;

    @Value("${sdcct.build.module.sdcct-web-gateway.git.build.version}")
    private String gitModuleBuildVersion;

    @Autowired
    private List<IheTestcase> iheTestcases;

    private long buildTimestamp;
    private Map<SpecificationRole, List<IheTestcase>> iheTestcasesMap = new HashMap<>();
    private Map<SpecificationRole, String> iheEndpointAddressesMap = new HashMap<>();
    private Map<SpecificationRole, String> iheTestcasesProcessUrlsMap = new HashMap<>();

    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH, (SdcctStringUtils.SLASH + SdcctViewNames.HOME) })
    public ModelAndView displayHome(HttpServletRequest servletReq) throws Exception {
        return new ModelAndView(SdcctViewNames.HOME, Collections.singletonMap(SdcctModelAttributes.BUILD_TIMESTAMP_NAME,
            SdcctDateFormatUtils.format(SdcctDateFormatUtils.DISPLAY_FORMAT, this.buildTimestamp, RequestContextUtils.getTimeZone(servletReq))));
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

    @ModelAttribute(SdcctModelAttributes.BUILD_VERSION_NAME)
    private String getBuildVersion() {
        return this.gitModuleBuildVersion;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.buildTimestamp = SdcctDateFormatUtils.parse(this.gitModuleBuildTime, null, null).getValue().getTime();

        this.iheTestcasesMap = this.iheTestcases.stream().sorted(OrderComparator.INSTANCE).collect(Collectors.groupingBy(IheTestcase::getRoleTested));

        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_ARCHIVER, this.rfdFormArchiverEndpointAddr);
        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_MANAGER, this.rfdFormManagerEndpointAddr);
        this.iheEndpointAddressesMap.put(SpecificationRole.FORM_RECEIVER, this.rfdFormReceiverEndpointAddr);

        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_ARCHIVER, this.iheFormArchiverProcessUrl);
        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_MANAGER, this.iheFormManagerProcessUrl);
        this.iheTestcasesProcessUrlsMap.put(SpecificationRole.FORM_RECEIVER, this.iheFormReceiverProcessUrl);
    }
}
