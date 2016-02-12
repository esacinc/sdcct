package gov.hhs.onc.sdcct.web.form.archiver.impl;

import gov.hhs.onc.sdcct.form.receiver.FormReceiver;
import gov.hhs.onc.sdcct.rfd.AnyXMLContentType;
import gov.hhs.onc.sdcct.rfd.RfdFormReceiverPortType;
import gov.hhs.onc.sdcct.rfd.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.SubmitFormResponseType;
import gov.hhs.onc.sdcct.rfd.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.rfd.impl.SubmitFormResponseTypeImpl;
import gov.hhs.onc.sdcct.web.form.receiver.RfdFormReceiverWebService;
import gov.hhs.onc.sdcct.xml.SdcctXmlNs;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@Component("wsFormReceiverRfdImpl")
@WebService(portName = RfdWsXmlNames.FORM_RECEIVER_PORT, serviceName = RfdWsXmlNames.FORM_RECEIVER_SERVICE, targetNamespace = SdcctXmlNs.IHE_ITI_RFD_URI)
public class RfdFormArchiverWebServiceImpl extends AbstractRfdFormWebService<FormReceiver> implements RfdFormReceiverPortType, RfdFormReceiverWebService {
    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public SubmitFormResponseType submitForm(AnyXMLContentType body) {
        return new SubmitFormResponseTypeImpl().setResponseCode(Integer.toString(HttpStatus.OK.value()));
    }
}
