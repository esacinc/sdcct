package gov.hhs.onc.sdcct.web.context.impl;

import java.util.Enumeration;
import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;

public class SdcctWebApplicationContext extends AnnotationConfigEmbeddedWebApplicationContext {
    private class SdcctServletConfig implements ServletConfig {
        @Nullable
        @Override
        public String getInitParameter(String name) {
            return SdcctWebApplicationContext.this.getServletContext().getInitParameter(name);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return SdcctWebApplicationContext.this.getServletContext().getInitParameterNames();
        }

        @Override
        public ServletContext getServletContext() {
            return SdcctWebApplicationContext.this.getServletContext();
        }

        @Override
        public String getServletName() {
            return SdcctWebApplicationContext.this.getServletContext().getServletContextName();
        }
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        SdcctServletConfig servletConfig = new SdcctServletConfig();
        this.setServletConfig(servletConfig);

        beanFactory.registerSingleton(AbstractRefreshableWebApplicationContext.SERVLET_CONFIG_BEAN_NAME, servletConfig);
        beanFactory.ignoreDependencyInterface(ServletConfigAware.class);

        super.postProcessBeanFactory(beanFactory);
    }
}
