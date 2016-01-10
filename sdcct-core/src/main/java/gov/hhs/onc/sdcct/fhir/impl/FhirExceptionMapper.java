package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.logging.impl.RootCauseThrowableProxyConverter;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

@Component("exceptionMapperFhir")
@Priority(0)
public class FhirExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        return Response.serverError().entity(RootCauseThrowableProxyConverter.buildStackTrace(exception)).build();
    }
}
