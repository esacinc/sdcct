package gov.hhs.onc.sdcct.web.controller.impl;

import gov.hhs.onc.sdcct.net.mime.SdcctMediaTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("controllerResource")
public class ResourceController {
    @Value((ResourceUtils.CLASSPATH_URL_PREFIX + "${sdcct.static.images.favicon.file.path}"))
    private Resource faviconFile;

    @RequestMapping(method = { RequestMethod.GET }, produces = { SdcctMediaTypes.IMAGE_X_ICON_VALUE },
        value = { "${sdcct.static.images.favicon.file.url.path}" })
    @ResponseBody
    public Resource displayFavicon() {
        return this.faviconFile;
    }
}
