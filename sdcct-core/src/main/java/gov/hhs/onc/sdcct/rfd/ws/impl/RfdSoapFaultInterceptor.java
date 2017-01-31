package gov.hhs.onc.sdcct.rfd.ws.impl;

import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.validate.ValidationException;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.ws.WsXmlQnames;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.qname.utils.SdcctQnameUtils;
import javax.xml.transform.dom.DOMResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.Soap12FaultOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component("interceptorSoapFaultRfd")
public class RfdSoapFaultInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
    private final static Logger LOGGER = LoggerFactory.getLogger(RfdSoapFaultInterceptor.class);

    @Autowired
    private XmlCodec xmlCodec;

    public RfdSoapFaultInterceptor() {
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
        // noinspection ThrowableResultOfMethodCallIgnored
        ValidationException faultValidationCause = SdcctExceptionUtils.findCause(faultCause, ValidationException.class);

        if (faultValidationCause != null) {
            try {
                this.xmlCodec.encode(faultValidationCause.getResult(), new DOMResult(faultDetailElem), null);
            } catch (Exception e) {
                LOGGER.error("Unable to build SOAP Fault Detail validation result child element.", e);
            }
        }

        Element faultDetailStacktraceElem =
            faultDetailDoc.createElementNS(SdcctUris.SDCCT_WS_URN_VALUE, SdcctQnameUtils.buildQualifiedName(WsXmlQnames.STACK_TRACE));
        faultDetailStacktraceElem
            .appendChild(faultDetailDoc.createCDATASection((StringUtils.LF + SdcctExceptionUtils.buildRootCauseStackTrace(faultCause) + StringUtils.LF)));
        faultDetailElem.appendChild(faultDetailStacktraceElem);
    }
}
