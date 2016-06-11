package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.rfd.RetrieveClarificationRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.rfd.RfdFormManagerPortType;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.FormDataTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.ws.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignPkgTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.FormPackageModulesImpl;
import gov.hhs.onc.sdcct.sdc.impl.FormPackageTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.PackageModulesTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.PackageTypeImpl;
import gov.hhs.onc.sdcct.web.form.manager.RfdFormManagerWebService;
import gov.hhs.onc.sdcct.xml.impl.XdmDocumentDestination;
import javax.jws.WebService;
import org.apache.cxf.interceptor.Fault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component("wsFormManagerRfdImpl")
@WebService(portName = RfdWsXmlNames.FORM_MANAGER_PORT, serviceName = RfdWsXmlNames.FORM_MANAGER_SERVICE, targetNamespace = SdcctUris.IHE_ITI_RFD_URN_VALUE)
public class RfdFormManagerWebServiceImpl extends AbstractRfdFormWebService<FormManager> implements RfdFormManagerPortType, RfdFormManagerWebService {
    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveForm(RetrieveFormRequestType reqParams) {
        String reqFormIdentifier = reqParams.getWorkflowData().getFormId();

        try {
            FormDesignType formDesign = this.service.findFormDesign(reqFormIdentifier);

            if (formDesign == null) {
                throw new Fault(new Exception(String.format("Form (identifier=%s) is unavailable.", reqFormIdentifier)));
            }

            return new RetrieveFormResponseTypeImpl()
                .setForm(
                    new FormDataTypeImpl().setContent(new AnyXmlContentTypeImpl().addAny(this.xmlCodec
                        .encode(
                            new PackageTypeImpl().setPackageModules(new PackageModulesTypeImpl().setMainFormPackage(new FormPackageTypeImpl()
                                .setFormPackageModules(new FormPackageModulesImpl().setFormDesignPkg(new FormDesignPkgTypeImpl().setFormDesign(formDesign))))),
                            new XdmDocumentDestination(this.config).getReceiver(), null).getXdmNode().getDocument().getDocumentElement())))
                .setContentType(MimeTypeUtils.APPLICATION_XML_VALUE).setResponseCode(Integer.toString(HttpStatus.OK.value()));
        } catch (Fault e) {
            throw e;
        } catch (Exception e) {
            throw new Fault(new Exception(String.format("Unable to retrieve form (identifier=%s)", reqFormIdentifier), e));
        }
    }

    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveClarification(RetrieveClarificationRequestType reqParams) {
        throw new Fault(new Exception("Operation is unsupported."));
    }
}
