package gov.hhs.onc.sdcct.web.context.impl;

import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.context.impl.SdcctApplicationConfiguration;
import javax.servlet.ServletContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.web.ServletContextApplicationContextInitializer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

public class WebServletInitializer extends SpringBootServletInitializer {
    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        SdcctApplication app = SdcctApplicationConfiguration.buildApplication();
        app.setApplicationContextClass(SdcctWebApplicationContext.class);
        app.setWebEnvironment(true);
        app.addInitializers(new ServletContextApplicationContextInitializer(servletContext));

        return this.run(app);
    }

    @Override
    protected SdcctWebApplicationContext run(SpringApplication app) {
        return ((SdcctWebApplicationContext) super.run(app));
    }
}
