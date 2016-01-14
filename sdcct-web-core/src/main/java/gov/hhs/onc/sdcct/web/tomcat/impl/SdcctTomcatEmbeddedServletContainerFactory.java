package gov.hhs.onc.sdcct.web.tomcat.impl;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.net.ssl.SSLEngine;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Service;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.net.NioEndpoint;
import org.apache.tomcat.util.net.SocketStatus;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class SdcctTomcatEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {
    public static class SdcctRequestFacade extends RequestFacade {
        private SdcctRequest req;

        public SdcctRequestFacade(SdcctRequest req) {
            super(req);

            this.req = req;
        }

        public SdcctRequest getRequest() {
            return this.req;
        }
    }

    public static class SdcctRequest extends Request {
        @Override
        public HttpServletRequest getRequest() {
            return ((this.facade != null) ? this.facade : (this.facade = new SdcctRequestFacade(this)));
        }
    }

    public static class SdcctResponseFacade extends ResponseFacade {
        private SdcctResponse resp;

        public SdcctResponseFacade(SdcctResponse resp) {
            super(resp);

            this.resp = resp;
        }

        public SdcctResponse getResponse() {
            return this.resp;
        }
    }

    public static class SdcctResponse extends Response {
        @Override
        public HttpServletResponse getResponse() {
            return ((this.facade != null) ? this.facade : (this.facade = new SdcctResponseFacade(this)));
        }
    }

    public static class SdcctNioEndpoint extends NioEndpoint {
        @Override
        protected boolean processSocket(@Nullable KeyAttachment attachment, SocketStatus socketStatus, boolean dispatch) {
            return super.processSocket(attachment, socketStatus, dispatch);
        }

        @Override
        protected SSLEngine createSSLEngine() {
            SSLEngine engine = this.getSSLContext().createSSLEngine();
            engine.setUseClientMode(false);

            return engine;
        }
    }

    public static class SdcctHttp11NioProtocol extends Http11NioProtocol {
        public SdcctHttp11NioProtocol() {
            super();

            ((SdcctNioEndpoint) (this.endpoint = new SdcctNioEndpoint())).setHandler(((Http11ConnectionHandler) this.getHandler()));
        }
    }

    public static class SdcctConnector extends Connector {
        public SdcctConnector() {
            super(SdcctHttp11NioProtocol.class.getName());

            this.protocolHandlerClassName = Http11NioProtocol.class.getName();
        }

        @Override
        public Response createResponse() {
            SdcctResponse resp = new SdcctResponse();
            resp.setConnector(this);

            return resp;
        }

        @Override
        public Request createRequest() {
            SdcctRequest req = new SdcctRequest();
            req.setConnector(this);

            return req;
        }
    }

    private File baseDir;
    private int connTimeout;
    private int maxConns;
    private int maxConnThreads;
    private Tomcat tomcat;

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer ... servletContextInits) {
        (this.tomcat = new Tomcat()).setBaseDir(this.baseDir.getAbsolutePath());

        Service service = this.tomcat.getService();

        Connector conn = new SdcctConnector();
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

    public void setValves(Valve ... valves) {
        this.setContextValves(Stream.of(valves).sorted(AnnotationAwareOrderComparator.INSTANCE).collect(Collectors.toList()));
    }
}
