package gov.hhs.onc.sdcct.fhir.ws;

import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.search.FhirSearchService;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirInteractionWsMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirResourceWsMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirWsMetadata;
import gov.hhs.onc.sdcct.form.ws.FormWebService;
import gov.hhs.onc.sdcct.net.mime.SdcctMediaTypes;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.springframework.http.MediaType;

public interface FhirFormWebService
    extends FormWebService<FhirResource, FhirResourceRegistry<?>, FhirSearchService<?>, FhirInteractionWsMetadata, FhirResourceWsMetadata<?>, FhirWsMetadata> {
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    @Path(FhirWsPaths.SEARCH_TYPE)
    @POST
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Response search(@PathParam(FhirWsPathParamNames.TYPE) String type, MultivaluedMap<String, String> params) throws Exception;

    @GET
    @Path(FhirWsPaths.TYPE)
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Response search(@PathParam(FhirWsPathParamNames.TYPE) String type) throws Exception;

    @Path(FhirWsPaths.TYPE)
    @POST
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Response create(@PathParam(FhirWsPathParamNames.TYPE) String type) throws Exception;

    @DELETE
    @Path(FhirWsPaths.INSTANCE)
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Response delete(@PathParam(FhirWsPathParamNames.TYPE) String type, @PathParam(FhirWsPathParamNames.ID) String id) throws Exception;

    @Path(FhirWsPaths.INSTANCE)
    @PUT
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Response update(@PathParam(FhirWsPathParamNames.TYPE) String type, @PathParam(FhirWsPathParamNames.ID) String id) throws Exception;

    @GET
    @Path(FhirWsPaths.HISTORY_INSTANCE)
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Response vread(@PathParam(FhirWsPathParamNames.TYPE) String type, @PathParam(FhirWsPathParamNames.ID) String id,
        @PathParam(FhirWsPathParamNames.VERSION) String version) throws Exception;

    @GET
    @Path(FhirWsPaths.INSTANCE)
    @Produces({ SdcctMediaTypes.APP_JSON_FHIR_VALUE, SdcctMediaTypes.APP_XML_FHIR_VALUE })
    public Response read(@PathParam(FhirWsPathParamNames.TYPE) String type, @PathParam(FhirWsPathParamNames.ID) String id) throws Exception;
}
