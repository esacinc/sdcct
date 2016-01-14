package gov.hhs.onc.sdcct.ws.impl;

import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.ws.SdcctWsContextPropertyNames;
import gov.hhs.onc.sdcct.ws.SdcctWsXmlQnames;
import gov.hhs.onc.sdcct.xml.SdcctXmlNs;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlQnameUtils;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.Soap12FaultOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component("interceptorFaultStackTrace")
public class FaultStackTraceInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
    public FaultStackTraceInterceptor() {
        super(Phase.PREPARE_SEND);

        this.setBefore(Collections.singletonList(Soap12FaultOutInterceptor.class.getName()));
    }

    @Override
    public void handleMessage(SoapMessage msg) throws Fault {
        // noinspection ThrowableResultOfMethodCallIgnored
        Fault fault = ((Fault) msg.getContent(Exception.class));
        Throwable faultCause;

        if (((faultCause = fault.getCause()) == null) || !MessageUtils.getContextualBoolean(msg, SdcctWsContextPropertyNames.ERROR_STACK_TRACE, false)) {
            return;
        }

        Element faultDetailElem = fault.getOrCreateDetail();
        Document faultDetailDoc = faultDetailElem.getOwnerDocument();

        Element faultDetailStacktraceElem =
            faultDetailDoc.createElementNS(SdcctXmlNs.SDCCT_URI, SdcctXmlQnameUtils.buildQualifiedName(SdcctWsXmlQnames.STACK_TRACE));
        faultDetailStacktraceElem
            .appendChild(faultDetailDoc.createCDATASection((StringUtils.LF + SdcctExceptionUtils.buildRootCauseStackTrace(faultCause) + StringUtils.LF)));
        faultDetailElem.appendChild(faultDetailStacktraceElem);
    }
}
