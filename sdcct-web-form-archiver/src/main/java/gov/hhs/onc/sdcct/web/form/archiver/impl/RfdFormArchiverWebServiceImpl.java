package gov.hhs.onc.sdcct.web.form.archiver.impl;

import gov.hhs.onc.sdcct.form.archiver.FormArchiver;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.ArchiveFormResponseType;
import gov.hhs.onc.sdcct.rfd.RfdFormArchiverPortType;
import gov.hhs.onc.sdcct.rfd.impl.ArchiveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.ws.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.web.form.archiver.RfdFormArchiverWebService;
import javax.jws.WebService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("wsFormArchiverRfdImpl")
@WebService(portName = RfdWsXmlNames.FORM_ARCHIVER_PORT, serviceName = RfdWsXmlNames.FORM_ARCHIVER_SERVICE, targetNamespace = SdcctUris.IHE_ITI_RFD_URN_VALUE)
public class RfdFormArchiverWebServiceImpl extends AbstractRfdFormWebService<FormArchiver> implements RfdFormArchiverPortType, RfdFormArchiverWebService {
    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public ArchiveFormResponseType archiveForm(AnyXmlContentType body) {
        return new ArchiveFormResponseTypeImpl().setResponseCode(Integer.toString(HttpStatus.OK.value()));
    }
}
