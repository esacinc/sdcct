package gov.hhs.onc.sdcct.web.form.receiver.impl;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.receiver.FormReceiver;
import gov.hhs.onc.sdcct.rfd.*;
import gov.hhs.onc.sdcct.rfd.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.rfd.impl.AnyXMLContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.FormDataTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.SubmitFormResponseTypeImpl;
import gov.hhs.onc.sdcct.web.form.receiver.RfdFormReceiverWebService;
import gov.hhs.onc.sdcct.xml.SdcctXmlNs;
import org.apache.cxf.interceptor.Fault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import javax.jws.WebService;
import java.util.Collections;

@Component("wsFormReceiverRfdImpl")
@WebService(portName = RfdWsXmlNames.FORM_RECEIVER_PORT, serviceName = RfdWsXmlNames.FORM_RECEIVER_SERVICE, targetNamespace = SdcctXmlNs.IHE_ITI_RFD_URI)
public class RfdFormReceiverWebServiceImpl extends AbstractRfdFormWebService<FormReceiver> implements RfdFormReceiverPortType, RfdFormReceiverWebService {
    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public SubmitFormResponseType submitForm(AnyXMLContentType body) {
        return new SubmitFormResponseTypeImpl().setResponseCode(Integer.toString(HttpStatus.OK.value()));

    }
}
