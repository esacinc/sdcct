package gov.hhs.onc.sdcct.web.form.manager.rfd.ws.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.rfd.EncodedResponse;
import gov.hhs.onc.sdcct.rfd.FormDataType;
import gov.hhs.onc.sdcct.rfd.RetrieveClarificationRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.rfd.RfdFormManagerPortType;
import gov.hhs.onc.sdcct.rfd.WorkflowDataType;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.FormDataTypeImpl;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsException;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsResponseType;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.ws.impl.AbstractRfdFormWebService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.SdcRetrieveFormPackageType;
import gov.hhs.onc.sdcct.sdc.impl.SdcRetrieveFormPackageTypeImpl;
import gov.hhs.onc.sdcct.sdc.impl.XmlPackageImpl;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.web.form.manager.rfd.ws.RfdFormManagerWebService;
import gov.hhs.onc.sdcct.xml.impl.XdmDocumentDestination;
import gov.hhs.onc.sdcct.xml.xslt.impl.SdcctXsltExecutable;
import java.util.Collections;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.ws.rs.core.MultivaluedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

@WebService(portName = RfdWsXmlNames.FORM_MANAGER_PORT, serviceName = RfdWsXmlNames.FORM_MANAGER_SERVICE, targetNamespace = SdcctUris.IHE_ITI_RFD_URN_VALUE)
public class RfdFormManagerWebServiceImpl extends AbstractRfdFormWebService implements RfdFormManagerPortType, RfdFormManagerWebService {
    @Resource(name = "xsltExecRfdFormHtml")
    private SdcctXsltExecutable formHtmlXsltExec;

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED, "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveForm(RetrieveFormRequestType reqParams) {
        if (!reqParams.hasWorkflowData()) {
            throw new RfdWsException("A workflowData element must be provided.");
        }

        WorkflowDataType workflowData = reqParams.getWorkflowData();
        EncodedResponse encResp = workflowData.getEncodedResponse();
        String identifier = workflowData.getFormId();
        RfdWsResponseType respType;

        if (encResp.getValue()) {
            if (!encResp.hasResponseContentType()) {
                throw new RfdWsException("A responseContentType attribute value must be provided.");
            }

            String reqRespContentTypeValue = encResp.getResponseContentType();
            MediaType reqRespContentType = MediaType.parseMediaType(encResp.getResponseContentType());

            // noinspection ConstantConditions
            if (reqRespContentType.equals(RfdWsResponseType.XML.getMediaType())) {
                respType = RfdWsResponseType.XML;
            } else // noinspection ConstantConditions
            if (reqRespContentType.equals(RfdWsResponseType.HTML.getMediaType())) {
                respType = RfdWsResponseType.HTML;
            } else {
                throw new RfdWsException(String.format("Invalid encodedResponse responseContentType attribute value: %s", reqRespContentTypeValue));
            }
        } else {
            respType = RfdWsResponseType.UNSTRUCTURED;
        }

        try {
            FormDesignType formDesign = this.findBean(FormDesignType.class, this.searchServices.get(FormDesignType.class),
                new MultivaluedHashMap<>(Collections.singletonMap(ResourceParamNames.IDENTIFIER, identifier)));

            if (formDesign == null) {
                throw new RfdWsException(String.format("Form (identifier=%s) not found.", identifier));
            }

            FormDataType formData = new FormDataTypeImpl();

            if (respType == RfdWsResponseType.UNSTRUCTURED) {
                // TODO: implement
                formData.setContent(StringUtils.EMPTY);
            } else {
                SdcRetrieveFormPackageType retrieveFormPkg = new SdcRetrieveFormPackageTypeImpl();

                if (respType == RfdWsResponseType.HTML) {
                    retrieveFormPkg.addContent(
                        ((Object) this.htmlTranscoder.transcode(this.formHtmlXsltExec, new ByteArraySource(this.xmlCodec.encode(formDesign, null)), null)));
                } else {
                    retrieveFormPkg.addContent(new XmlPackageImpl().setFormDesign(formDesign));
                }

                formData.setContent(new AnyXmlContentTypeImpl().addAny(this.xmlCodec
                    .encode(retrieveFormPkg, new XdmDocumentDestination(this.config).getReceiver(), null).getXdmNode().getDocument().getDocumentElement()));
            }

            return new RetrieveFormResponseTypeImpl().setForm(formData).setContentType(respType.getId()).setResponseCode(StringUtils.EMPTY);
        } catch (RfdWsException e) {
            throw e;
        } catch (Exception e) {
            throw new RfdWsException(String.format("Unable to retrieve form (identifier=%s)", identifier), e);
        }
    }

    @Override
    @SuppressWarnings({ "ValidExternallyBoundObject" })
    public RetrieveFormResponseType retrieveClarification(RetrieveClarificationRequestType reqParams) {
        throw new RfdWsException("RetrieveClarification operation is not supported.");
    }
}
