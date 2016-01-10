package gov.hhs.onc.sdcct.web.form.manager;

import gov.hhs.onc.sdcct.fhir.FhirFormWebService;
import gov.hhs.onc.sdcct.fhir.FhirWsPathParamNames;
import gov.hhs.onc.sdcct.fhir.FhirWsResourceNames;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.io.SdcctMediaTypes;
import gov.hhs.onc.sdcct.net.utils.SdcctUriUtils;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public interface FhirFormManagerWebService extends FhirFormWebService<FormManager> {
    @GET
    @Path(SdcctUriUtils.PATH_DELIM + FhirWsResourceNames.QUESTIONNAIRE + SdcctUriUtils.PATH_DELIM + SdcctUriUtils.PATH_PARAM_PREFIX + FhirWsPathParamNames.ID
        + SdcctUriUtils.PATH_PARAM_SUFFIX)
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Questionnaire readQuestionnaire(@PathParam(FhirWsPathParamNames.ID) String questionnaireId) throws Exception;
}
