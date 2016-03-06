package gov.hhs.onc.sdcct.web.form.manager;

import gov.hhs.onc.sdcct.fhir.Bundle;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.ws.FhirFormWebService;
import gov.hhs.onc.sdcct.fhir.ws.FhirWsPathParamNames;
import gov.hhs.onc.sdcct.fhir.ws.FhirWsPathParts;
import gov.hhs.onc.sdcct.fhir.ws.FhirWsResourceNames;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.io.SdcctMediaTypes;
import gov.hhs.onc.sdcct.net.utils.SdcctUriUtils;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import org.springframework.http.MediaType;

public interface FhirFormManagerWebService extends FhirFormWebService<FormManager> {
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    @Path((SdcctUriUtils.PATH_DELIM + FhirWsResourceNames.QUESTIONNAIRE + SdcctUriUtils.PATH_DELIM + FhirWsPathParts.SEARCH))
    @POST
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Bundle findQuestionnaires(MultivaluedMap<String, String> formParams) throws Exception;

    @GET
    @Path((SdcctUriUtils.PATH_DELIM + FhirWsResourceNames.QUESTIONNAIRE))
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Bundle findQuestionnaires() throws Exception;

    @GET
    @Path((SdcctUriUtils.PATH_DELIM + FhirWsResourceNames.QUESTIONNAIRE + SdcctUriUtils.PATH_DELIM + SdcctUriUtils.PATH_PARAM_PREFIX + FhirWsPathParamNames.ID + SdcctUriUtils.PATH_PARAM_SUFFIX))
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public
        Questionnaire findQuestionnaire(@PathParam(FhirWsPathParamNames.ID) String questionnaireId) throws Exception;
}
