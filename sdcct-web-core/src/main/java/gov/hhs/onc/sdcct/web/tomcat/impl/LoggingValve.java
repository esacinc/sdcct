package gov.hhs.onc.sdcct.web.tomcat.impl;

import gov.hhs.onc.sdcct.logging.impl.AtomicEventId;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("valveLogging")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingValve extends ValveBase {
    private final static AtomicEventId EVENT_ID = new AtomicEventId();

    @Override
    public void invoke(Request req, Response resp) throws IOException, ServletException {
        // TODO: implement
        //MDC.put(EventIdMdcConverter.EVENT_ID_MDC_KEY, Long.toString(EVENT_ID.getNext()));

        try {
            this.getNext().invoke(req, resp);
        } finally {
            //MDC.remove(EventIdMdcConverter.EVENT_ID_MDC_KEY);
        }
    }
}
