package gov.hhs.onc.sdcct.fhir.ws.impl;

import gov.hhs.onc.sdcct.fhir.NarrativeStatus;
import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeImpl;
import gov.hhs.onc.sdcct.fhir.impl.NarrativeStatusComponentImpl;
import gov.hhs.onc.sdcct.fhir.ws.utils.SdcctFhirOperationOutcomeUtils.OperationOutcomeBuilder;
import gov.hhs.onc.sdcct.fhir.ws.utils.SdcctFhirOperationOutcomeUtils.OperationOutcomeIssueBuilder;
import gov.hhs.onc.sdcct.fhir.ws.FhirWsException;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.DivImpl;
import gov.hhs.onc.sdcct.fhir.xhtml.impl.PreImpl;
import gov.hhs.onc.sdcct.net.http.SdcctHttpStatus;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.nio.charset.StandardCharsets;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("provExceptionMapperFhir")
@Priority(0)
public class FhirExceptionMapper implements ExceptionMapper<Throwable> {
    private final static Logger LOGGER = LoggerFactory.getLogger(FhirExceptionMapper.class);

    @Autowired
    private XmlCodec xmlCodec;

    @Override
    public Response toResponse(Throwable exception) {
        SdcctHttpStatus respStatus = SdcctHttpStatus.INTERNAL_SERVER_ERROR;
        OperationOutcome opOutcome = null;

        if (exception instanceof FhirWsException) {
            FhirWsException wsException = ((FhirWsException) exception);

            respStatus = wsException.getResponseStatus();

            if (wsException.hasOperationOutcome()) {
                opOutcome = wsException.getOperationOutcome();
            }
        }

        if (opOutcome == null) {
            opOutcome = new OperationOutcomeBuilder().build();
        }

        if (!opOutcome.hasIssues()) {
            opOutcome.addIssues(new OperationOutcomeIssueBuilder().build());
        }

        if (MessageUtils.getContextualBoolean(JAXRSUtils.getCurrentMessage(), WsPropertyNames.ERROR_STACK_TRACE, false)) {
            try {
                opOutcome.setText(new NarrativeImpl()
                    .setDiv(new DivImpl().addContent(new String(
                        this.xmlCodec.encode(new PreImpl().addContent(SdcctExceptionUtils.buildRootCauseStackTrace(exception)), null), StandardCharsets.UTF_8)))
                    .setStatus(new NarrativeStatusComponentImpl().setValue(NarrativeStatus.ADDITIONAL)));
            } catch (Exception e) {
                LOGGER.error("Unable to encode FHIR operation outcome narrative.", e);
            }
        }

        return Response.status(respStatus).entity(opOutcome).build();
    }
}
