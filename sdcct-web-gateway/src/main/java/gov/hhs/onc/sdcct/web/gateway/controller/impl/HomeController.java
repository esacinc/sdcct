package gov.hhs.onc.sdcct.web.gateway.controller.impl;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.web.gateway.controller.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("controllerHome")
public class HomeController {
    @RequestMapping(method = { RequestMethod.GET }, value = { SdcctStringUtils.SLASH, (SdcctStringUtils.SLASH + ViewNames.HOME) })
    public ModelAndView displayHome() throws Exception {
        return new ModelAndView(ViewNames.HOME);
    }
}
