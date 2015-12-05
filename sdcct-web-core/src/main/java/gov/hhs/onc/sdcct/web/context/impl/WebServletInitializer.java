package gov.hhs.onc.sdcct.web.context.impl;

import gov.hhs.onc.sdcct.context.SdcctProperties;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.context.impl.SdcctApplicationConfiguration;
import java.io.File;
import java.util.Optional;
import javax.servlet.ServletContext;
import org.springframework.boot.context.web.ServletContextApplicationContextInitializer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

public class WebServletInitializer extends SpringBootServletInitializer {
    private final static String WEBAPP_HOME_RESOLVE_PATH = "/";
    private final static String WEBAPP_HOME_CONTEXT_PATH = "/WEB-INF";

    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        SdcctApplication app = SdcctApplicationConfiguration.buildApplication();
        app.setWebEnvironment(true);

        app.setHomeDirectory(new File(Optional.ofNullable(System.getProperty(SdcctProperties.APP_HOME_DIR_NAME))
            .orElseGet(() -> (servletContext.getRealPath(WEBAPP_HOME_RESOLVE_PATH) + WEBAPP_HOME_CONTEXT_PATH))));

        app.addInitializers(new ServletContextApplicationContextInitializer(servletContext));

        return this.run(app);
    }
}
