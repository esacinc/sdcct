package gov.hhs.onc.sdcct.context.impl;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

@Configuration("appConfiguration")
public abstract class SdcctApplicationConfiguration {
    private static class SdcctApplicationBuilder extends SpringApplicationBuilder {
        public SdcctApplicationBuilder(Object ... srcs) {
            super(srcs);
        }

        @Override
        protected SdcctApplication createSpringApplication(Object ... srcs) {
            return new SdcctApplication(srcs);
        }
    }

    private final static String SRCS_RESOURCE_LOC_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "META-INF/sdcct/spring/spring-sdcct*.xml";
    private final static String PROP_SRCS_RESOURCE_LOC_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "META-INF/sdcct/sdcct*.properties";

    private final static String PROP_SRC_NAME = "appProperties";

    public static void main(String ... args) {
        buildApplication().run(args);
    }

    public static SdcctApplication buildApplication() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);

        SdcctApplication app = ((SdcctApplication) new SdcctApplicationBuilder(buildSources(resourcePatternResolver)).addCommandLineProperties(false)
            .bannerMode(Mode.OFF).headless(true).resourceLoader(resourceLoader).application());

        app.setPropertySource(buildPropertySource(resourcePatternResolver));

        app.setListeners(
            app.getListeners().stream().filter((appListener -> !appListener.getClass().equals(LoggingApplicationListener.class))).collect(Collectors.toList()));

        return app;
    }

    private static CompositePropertySource buildPropertySource(ResourcePatternResolver resourcePatternResolver) {
        Resource[] propSrcResources;

        try {
            propSrcResources = resourcePatternResolver.getResources(PROP_SRCS_RESOURCE_LOC_PATTERN);
        } catch (IOException e) {
            throw new ApplicationContextException(
                String.format("Unable to resolve application property source resource(s) for pattern: %s", PROP_SRCS_RESOURCE_LOC_PATTERN), e);
        }

        CompositePropertySource propSrc = new CompositePropertySource(PROP_SRC_NAME);

        for (Resource propSrcResource : propSrcResources) {
            try {
                propSrc.addFirstPropertySource(
                    new PropertiesPropertySource(propSrcResource.getURI().toString(), PropertiesLoaderUtils.loadProperties(propSrcResource)));
            } catch (IOException e) {
                throw new ApplicationContextException(String.format("Unable to load application property source resource (fileName=%s, desc=%s).",
                    propSrcResource.getFilename(), propSrcResource.getDescription()), e);
            }
        }

        return propSrc;
    }

    private static Object[] buildSources(ResourcePatternResolver resourcePatternResolver) {
        try {
            return Stream.concat(Stream.of(SdcctApplicationConfiguration.class), Stream.of(resourcePatternResolver.getResources(SRCS_RESOURCE_LOC_PATTERN)))
                .toArray(Object[]::new);
        } catch (IOException e) {
            throw new ApplicationContextException(String.format("Unable to resolve application source resource(s) for pattern: %s", SRCS_RESOURCE_LOC_PATTERN),
                e);
        }
    }
}
