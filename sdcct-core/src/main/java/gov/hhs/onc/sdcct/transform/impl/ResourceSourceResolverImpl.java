package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.transform.ResourceSourceResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

@Component("resourceSrcResolver")
public class ResourceSourceResolverImpl implements ResourceSourceResolver {
    private ResourcePatternResolver resourcePatternResolver;

    public ResourceSourceResolverImpl() {
        this(null);
    }

    public ResourceSourceResolverImpl(@Nullable ResourceLoader resourceLoader) {
        this.setResourceLoader(((resourceLoader != null) ? resourceLoader : new DefaultResourceLoader()));
    }

    @Override
    public ResourceSource[] resolveAll(String ... locs) throws IOException {
        List<ResourceSource> resourceSrcs = new ArrayList<>();

        for (String loc : locs) {
            for (Resource resource : this.resourcePatternResolver.getResources(loc)) {
                resourceSrcs.add(new ResourceSource(resource));
            }
        }

        return resourceSrcs.toArray(new ResourceSource[resourceSrcs.size()]);
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
