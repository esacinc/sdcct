package gov.hhs.onc.sdcct.io.impl;

import gov.hhs.onc.sdcct.io.ResourceSourceResolver;
import java.io.IOException;
import javax.annotation.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

@Component("resourceSrcResolverImpl")
public class ResourceSourceResolverImpl implements ResourceSourceResolver {
    private ResourcePatternResolver resourcePatternResolver;

    @Override
    public ResourceSource[] resolveAll(String loc) throws IOException {
        Resource[] resources = this.resourcePatternResolver.getResources(loc);
        ResourceSource[] resourceSrcs = new ResourceSource[resources.length];

        for (int a = 0; a < resources.length; a++) {
            resourceSrcs[a] = new ResourceSource(resources[a]);
        }

        return resourceSrcs;
    }

    @Nullable
    @Override
    public ResourceSource resolve(String loc) throws IOException {
        Resource[] resources = this.resourcePatternResolver.getResources(loc);

        return ((resources.length > 0) ? new ResourceSource(resources[0]) : null);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }
}
