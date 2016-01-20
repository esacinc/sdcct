package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.ws.WsXmlQnames;
import gov.hhs.onc.sdcct.xml.SdcctXmlNs;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlQnameUtils;
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

@Component("interceptorSoapFaultStackTrace")
public class SoapFaultStackTraceInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
    public SoapFaultStackTraceInterceptor() {
        super(Phase.PREPARE_SEND);

        this.addBefore(Soap12FaultOutInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage msg) throws Fault {
        // noinspection ThrowableResultOfMethodCallIgnored
        Fault fault = ((Fault) msg.getContent(Exception.class));
        Throwable faultCause;

        if (((faultCause = fault.getCause()) == null) || !MessageUtils.getContextualBoolean(msg, WsPropertyNames.ERROR_STACK_TRACE, false)) {
            return;
        }
        
        Element faultDetailElem = fault.getOrCreateDetail();
        Document faultDetailDoc = faultDetailElem.getOwnerDocument();

        Element faultDetailStacktraceElem =
            faultDetailDoc.createElementNS(SdcctXmlNs.SDCCT_URI, SdcctXmlQnameUtils.buildQualifiedName(WsXmlQnames.STACK_TRACE));
        faultDetailStacktraceElem
            .appendChild(faultDetailDoc.createCDATASection((StringUtils.LF + SdcctExceptionUtils.buildRootCauseStackTrace(faultCause) + StringUtils.LF)));
        faultDetailElem.appendChild(faultDetailStacktraceElem);
    }
}
