package gov.hhs.onc.sdcct.web.tomcat.impl;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

public class SdcctTomcatEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {
    private File baseDir;
    private int connTimeout;
    private int maxConns;
    private int maxConnThreads;
    private Tomcat tomcat;

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer ... servletContextInits) {
        (this.tomcat = new Tomcat()).setBaseDir(this.baseDir.getAbsolutePath());

        Service service = this.tomcat.getService();

        Connector conn = new Connector(Http11NioProtocol.class.getName());
        service.addConnector(conn);

        this.customizeConnector(conn);
        this.tomcat.setConnector(conn);

        Host host = this.tomcat.getHost();
        host.setAutoDeploy(false);

        this.tomcat.getEngine().setBackgroundProcessorDelay(-1);

        this.prepareContext(host, servletContextInits);

        return this.getTomcatEmbeddedServletContainer(this.tomcat);
    }

    @Override
    protected void customizeConnector(Connector conn) {
        super.customizeConnector(conn);

        Http11NioProtocol connProtocol = ((Http11NioProtocol) conn.getProtocolHandler());
        connProtocol.setConnectionTimeout(this.connTimeout);
        connProtocol.setMaxConnections(this.maxConns);
        connProtocol.setMaxThreads(this.maxConnThreads);
    }

    @Override
    protected void configureContext(Context context, ServletContextInitializer[] servletContextInits) {
        context.setDistributable(true);
        context.setIgnoreAnnotations(true);
        context.setTldValidation(true);
        context.setXmlNamespaceAware(true);
        context.setXmlValidation(true);

        super.configureContext(context, servletContextInits);
    }

    public File getBaseDirectory() {
        return this.baseDir;
    }

    @Override
    public void setBaseDirectory(File baseDir) {
        super.setBaseDirectory((this.baseDir = baseDir));
    }

    public int getConnectionTimeout() {
        return this.connTimeout;
    }

    public void setConnectionTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getMaxConnections() {
        return this.maxConns;
    }

    public void setMaxConnections(int maxConns) {
        this.maxConns = maxConns;
    }

    public int getMaxConnectionThreads() {
        return this.maxConnThreads;
    }

    public void setMaxConnectionThreads(int maxConnThreads) {
        this.maxConnThreads = maxConnThreads;
    }

    public Tomcat getTomcat() {
        return this.tomcat;
    }
}
