package gov.hhs.onc.sdcct.web.controller.impl;

import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.net.mime.SdcctMediaTypes;
import gov.hhs.onc.sdcct.transform.ResourceSourceResolver;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.utils.SdcctResourceUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

public class ResourceController {
    private final static String STATIC_RESOURCE_PATH_PREFIX = "/static";

    @Value((ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "${sdcct.static.images.favicon.file.path}"))
    private ResourceSource faviconFileSrc;

    @Value((ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "${sdcct.static.dir.path}" + SdcctStringUtils.SLASH))
    private Resource staticDir;

    @Autowired
    private ResourceSourceResolver resourceSrcResolver;

    @RequestMapping(method = { RequestMethod.GET }, produces = { SdcctMediaTypes.IMAGE_X_ICON_VALUE }, value = { ("/favicon." + SdcctFileNameExtensions.ICO) })
    public void displayFavicon(HttpServletResponse servletResp) throws Exception {
        displayResource(servletResp, this.faviconFileSrc);
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { (STATIC_RESOURCE_PATH_PREFIX + "/**") })
    public void displayStaticResource(HttpServletRequest servletReq, HttpServletResponse servletResp) throws Exception {
        String reqPath = ((String) servletReq.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        ResourceSource staticResourceSrc = this.resourceSrcResolver.resolve(SdcctResourceUtils.extractPath(
            this.staticDir.createRelative(StringUtils.removeStart(StringUtils.removeStart(reqPath, STATIC_RESOURCE_PATH_PREFIX), SdcctStringUtils.SLASH))));

        if (staticResourceSrc == null) {
            servletResp.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Static resource not found: %s", reqPath));

            return;
        }

        displayResource(servletResp, staticResourceSrc);
    }

    private static void displayResource(HttpServletResponse servletResp, ResourceSource resourceSrc) throws Exception {
        byte[] resourceContent = resourceSrc.getBytes();

        servletResp.setContentLength(resourceContent.length);

        IOUtils.write(resourceContent, servletResp.getOutputStream());
    }
}
