package gov.hhs.onc.sdcct.web.controller.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.webjars.WebJarAssetLocator;

@Controller("controllerWebJarLocator")
public class WebJarLocatorController {
    private static class CachedClassPathResource extends ClassPathResource {
        private byte[] content;

        public CachedClassPathResource(String path) throws IOException {
            super(path);

            if (this.exists()) {
                try (InputStream inStream = super.getInputStream()) {
                    content = IOUtils.toByteArray(inStream);
                }
            }
        }

        public byte[] getContent() {
            return this.content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.content);
        }
    }

    private final static String PATH_PREFIX = "/webjars-locator/";
    private final static String PATH_SUFFIX = "/";

    private final static int PATH_AFFIX_LEN = PATH_PREFIX.length() + PATH_SUFFIX.length();

    private final static WebJarAssetLocator ASSET_LOCATOR = new WebJarAssetLocator();

    private final static Map<String, CachedClassPathResource> ASSET_CACHE = new ConcurrentHashMap<>();

    @RequestMapping(value = { (PATH_PREFIX + "{webjar}" + PATH_SUFFIX + "**") }, method = { RequestMethod.GET })
    @ResponseBody
    public ResponseEntity<?> displayAsset(@PathVariable String webjar, HttpServletRequest servletReq) throws IOException {
        String assetPath;

        try {
            assetPath = ASSET_LOCATOR.getFullPath(webjar,
                ((String) servletReq.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).substring((PATH_AFFIX_LEN + webjar.length())));
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.notFound().build();
        }

        CachedClassPathResource assetResource;

        if (!ASSET_CACHE.containsKey(assetPath)) {
            if ((assetResource = new CachedClassPathResource(assetPath)).exists()) {
                ASSET_CACHE.put(assetPath, assetResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            assetResource = ASSET_CACHE.get(assetPath);
        }

        return ResponseEntity.ok().contentLength(assetResource.getContent().length).body(assetResource);
    }
}
