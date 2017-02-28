package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.testcases.SpecificationRole;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.utils.SdcctDateUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.gateway.controller.SdcctModelAttributes;
import gov.hhs.onc.sdcct.web.gateway.controller.ViewNames;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller("controllerHome")
public class HomeController implements InitializingBean {
    private final static String IHE_TESTCASE_ID_PREFIX = "IHE_";

    // TODO: update timestamp and version to be based on build properties and not hard-coded
    private long buildTimestamp;

    @Value("${sdcct.build.version}")
    private String buildVersion;

    @Resource(name = "timeZoneDefault")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private TimeZone defaultTimeZone;

    @Value("${sdcct.ws.form.manager.rfd.url}")
    private String rfdFormManagerEndpointAddr;

    @Value("${sdcct.ws.form.receiver.rfd.url}")
    private String rfdFormReceiverEndpointAddr;

    @Value("${sdcct.ws.form.archiver.rfd.url}")
    private String rfdFormArchiverEndpointAddr;

    @Autowired
    private List<IheTestcase> iheTestcases;

    private Map<SpecificationRole, List<IheTestcase>> iheTestcasesMap = new HashMap<>();
    private Map<SpecificationRole, String> endpointAddressesMap = new HashMap<>();

    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH, (SdcctStringUtils.SLASH + ViewNames.HOME) })
    public ModelAndView displayHome(HttpServletRequest servletRequest) throws Exception {
        this.buildTimestamp = Instant.now().toEpochMilli();

        return new ModelAndView(ViewNames.HOME,
            Collections.singletonMap(SdcctModelAttributes.FORMATTED_BUILD_TIMESTAMP_NAME, SdcctDateUtils.format(SdcctDateUtils.DISPLAY_FORMAT,
                this.buildTimestamp, ObjectUtils.defaultIfNull(RequestContextUtils.getTimeZone(servletRequest), this.defaultTimeZone))));
    }

    @ModelAttribute(value = SdcctModelAttributes.BUILD_VERSION_NAME)
    private String getBuildVersion() {
        return this.buildVersion;
    }

    @ModelAttribute(value = SdcctModelAttributes.IHE_TESTCASES_NAME)
    private Map<SpecificationRole, List<IheTestcase>> getIheTestcases() {
        return this.iheTestcasesMap;
    }

    @ModelAttribute(value = SdcctModelAttributes.IHE_ENDPOINT_ADDRESSES_NAME)
    private Map<SpecificationRole, String> getIheEndpointAddresses() {
        return this.endpointAddressesMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.iheTestcasesMap = this.iheTestcases.stream()
            .sorted(Comparator.comparingInt((IheTestcase iheTestcase) -> Integer.valueOf(iheTestcase.getId().substring(IHE_TESTCASE_ID_PREFIX.length()))))
            .collect(Collectors.groupingBy(IheTestcase::getRoleTested));

        this.endpointAddressesMap.put(SpecificationRole.FORM_ARCHIVER, this.rfdFormArchiverEndpointAddr);
        this.endpointAddressesMap.put(SpecificationRole.FORM_MANAGER, this.rfdFormManagerEndpointAddr);
        this.endpointAddressesMap.put(SpecificationRole.FORM_RECEIVER, this.rfdFormReceiverEndpointAddr);
    }
}
