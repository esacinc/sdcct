package gov.hhs.onc.sdcct.web.context.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.context.impl.SdcctApplicationConfiguration;
import java.io.File;
import java.util.Optional;
import javax.servlet.ServletContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.web.ServletContextApplicationContextInitializer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

public class WebServletInitializer extends SpringBootServletInitializer {
    private final static String WEBAPP_HOME_RESOLVE_PATH = "/";
    private final static String WEBAPP_HOME_CONTEXT_PATH = "/WEB-INF";

    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        SdcctApplication app = SdcctApplicationConfiguration.buildApplication();
        app.setApplicationContextClass(SdcctWebApplicationContext.class);
        app.setWebEnvironment(true);

        app.setHomeDirectory(new File(Optional.ofNullable(System.getProperty(SdcctPropertyNames.APP_HOME_DIR))
            .orElseGet(() -> (servletContext.getRealPath(WEBAPP_HOME_RESOLVE_PATH) + WEBAPP_HOME_CONTEXT_PATH))));

        app.addInitializers(new ServletContextApplicationContextInitializer(servletContext));

        return this.run(app);
    }

    @Override
    protected SdcctWebApplicationContext run(SpringApplication app) {
        return ((SdcctWebApplicationContext) super.run(app));
    }
}
