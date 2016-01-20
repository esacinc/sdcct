package gov.hhs.onc.sdcct.web.tomcat.impl;

import gov.hhs.onc.sdcct.data.impl.TxId;
import gov.hhs.onc.sdcct.logging.MdcPropertyNames;
import gov.hhs.onc.sdcct.logging.impl.TxTaskWrapper;
import java.io.File;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.net.ssl.SSLEngine;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Service;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.ClassUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.net.NioEndpoint;
import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

public class SdcctTomcatEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {
    public static class SdcctNioEndpoint extends NioEndpoint {
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
            TomcatResponse resp = new TomcatResponse();
            resp.setConnector(this);

            return resp;
        }

        @Override
        public Request createRequest() {
            TomcatRequest req = new TomcatRequest();
            req.setConnector(this);

            return req;
        }
    }

    public class SdcctThreadPoolExecutor extends ThreadPoolExecutor {
        public SdcctThreadPoolExecutor() {
            super(SdcctTomcatEmbeddedServletContainerFactory.this.minConnThreads, SdcctTomcatEmbeddedServletContainerFactory.this.maxConnThreads,
                SdcctTomcatEmbeddedServletContainerFactory.this.connKeepAliveTimeout, TimeUnit.SECONDS, new TaskQueue(), new TaskThreadFactory(
                    (SdcctTomcatEmbeddedServletContainerFactory.this.connEndpointName + CONN_EXEC_THREAD_NAME_PREFIX), true, Thread.NORM_PRIORITY));

            ((TaskQueue) this.getQueue()).setParent(this);
        }

        @Override
        public void execute(Runnable task) {
            super.execute((task.getClass().getName().equals(SOCKET_PROCESSOR_CLASS_NAME)
                ? new TxTaskWrapper(Collections.singletonMap(MdcPropertyNames.HTTP_TX_ID, SdcctTomcatEmbeddedServletContainerFactory.this.httpTxId), task)
                : task));
        }
    }

    private final static String CONN_EXEC_THREAD_NAME_PREFIX = "-exec-";

    private final static String SOCKET_PROCESSOR_CLASS_NAME = NioEndpoint.class.getName() + ClassUtils.INNER_CLASS_SEPARATOR_CHAR + "SocketProcessor";

    private File baseDir;
    private String connEndpointName;
    private int connKeepAliveTimeout;
    private int connTimeout;
    private TxId httpTxId;
    private int maxConns;
    private int maxConnThreads;
    private int minConnThreads;
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
        connProtocol.setExecutor(new SdcctThreadPoolExecutor());
        connProtocol.setConnectionTimeout(this.connTimeout);
        connProtocol.getEndpoint().setName(this.connEndpointName);
        connProtocol.setKeepAliveTimeout(this.connKeepAliveTimeout);
        connProtocol.setMaxConnections(this.maxConns);
        connProtocol.setMaxThreads(this.maxConnThreads);
        connProtocol.setMinSpareThreads(this.minConnThreads);
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

    public String getConnectionEndpointName() {
        return this.connEndpointName;
    }

    public void setConnectionEndpointName(String connEndpointName) {
        this.connEndpointName = connEndpointName;
    }

    @Nonnegative
    public int getConnectionKeepAliveTimeout() {
        return this.connKeepAliveTimeout;
    }

    public void setConnectionKeepAliveTimeout(@Nonnegative int connKeepAliveTimeout) {
        this.connKeepAliveTimeout = connKeepAliveTimeout;
    }

    @Nonnegative
    public int getConnectionTimeout() {
        return this.connTimeout;
    }

    public void setConnectionTimeout(@Nonnegative int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public TxId getHttpTxId() {
        return this.httpTxId;
    }

    public void setHttpTxId(TxId httpTxId) {
        this.httpTxId = httpTxId;
    }

    @Nonnegative
    public int getMaxConnections() {
        return this.maxConns;
    }

    public void setMaxConnections(@Nonnegative int maxConns) {
        this.maxConns = maxConns;
    }

    @Nonnegative
    public int getMaxConnectionThreads() {
        return this.maxConnThreads;
    }

    public void setMaxConnectionThreads(@Nonnegative int maxConnThreads) {
        this.maxConnThreads = maxConnThreads;
    }

    @Nonnegative
    public int getMinConnectionThreads() {
        return this.minConnThreads;
    }

    public void setMinConnectionThreads(@Nonnegative int minConnThreads) {
        this.minConnThreads = minConnThreads;
    }

    public Tomcat getTomcat() {
        return this.tomcat;
    }

    public void setValves(Valve ... valves) {
        this.setContextValves(Stream.of(valves).sorted(AnnotationAwareOrderComparator.INSTANCE).collect(Collectors.toList()));
    }
}
