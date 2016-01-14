package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.rfd.RetrieveClarificationRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.rfd.RfdFormManagerPortType;
import gov.hhs.onc.sdcct.rfd.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.rfd.impl.AnyXMLContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.FormDataTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.web.form.manager.RfdFormManagerWebService;
import gov.hhs.onc.sdcct.xml.SdcctXmlNs;
import java.util.Collections;
import javax.jws.WebService;
import org.apache.cxf.interceptor.Fault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component("wsFormManagerRfdImpl")
@WebService(portName = RfdWsXmlNames.FORM_MANAGER_PORT, serviceName = RfdWsXmlNames.FORM_MANAGER_SERVICE, targetNamespace = SdcctXmlNs.IHE_ITI_RFD_URI)
public class RfdFormManagerWebServiceImpl extends AbstractRfdFormWebService<FormManager> implements RfdFormManagerPortType, RfdFormManagerWebService {
    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveForm(RetrieveFormRequestType reqParams) {
        String reqFormId = reqParams.getWorkflowData().getFormID();

        try {
            Form form = this.service.retrieveForm(reqFormId);

            if (form == null) {
                throw new Fault(new Exception(String.format("Form (id=%s) is unavailable.", reqFormId)));
            }

            if (!form.isSetPackageElement()) {
                throw new Fault(new Exception(String.format("IHE SDC variant of the specified form (id=%s) is unavailable.", reqFormId)));
            }

            return new RetrieveFormResponseTypeImpl(new FormDataTypeImpl(new AnyXMLContentTypeImpl(Collections.singletonList(form.getPackageElement()))),
                MimeTypeUtils.APPLICATION_XML_VALUE, Integer.toString(HttpStatus.OK.value()));
        } catch (Fault e) {
            throw e;
        } catch (Exception e) {
            throw new Fault(new Exception(String.format("Unable to retrieve form (id=%s)", reqFormId), e));
        }
    }

    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveClarification(RetrieveClarificationRequestType reqParams) {
        throw new Fault(new Exception("Operation is unsupported."));
    }
}
