package gov.hhs.onc.sdcct.web.form.receiver.rfd.ws.impl;

import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.RfdFormReceiverPortType;
import gov.hhs.onc.sdcct.rfd.SubmitFormResponseType;
import gov.hhs.onc.sdcct.rfd.impl.SubmitFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.ws.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.web.form.receiver.rfd.ws.RfdFormReceiverWebService;
import javax.jws.WebService;
import org.apache.commons.lang3.StringUtils;

@WebService(portName = RfdWsXmlNames.FORM_RECEIVER_PORT, serviceName = RfdWsXmlNames.FORM_RECEIVER_SERVICE, targetNamespace = SdcctUris.IHE_ITI_RFD_URN_VALUE)
public class RfdFormReceiverWebServiceImpl extends AbstractRfdFormWebService implements RfdFormReceiverPortType, RfdFormReceiverWebService {
    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public SubmitFormResponseType submitForm(AnyXmlContentType body) {
        return new SubmitFormResponseTypeImpl().setResponseCode(StringUtils.EMPTY);
    }
}
