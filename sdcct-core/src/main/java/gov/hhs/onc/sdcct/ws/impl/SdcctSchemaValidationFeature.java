package gov.hhs.onc.sdcct.ws.impl;

import org.apache.cxf.Bus;
import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

@Component("featureSchemaValidation")
public class SdcctSchemaValidationFeature extends AbstractFeature {
    @Override
    public void initialize(Client client, Bus bus) {
        initialize(client.getEndpoint());
    }

    @Override
    public void initialize(Server server, Bus bus) {
        initialize(server.getEndpoint());
    }

    private static void initialize(Endpoint endpoint) {
        endpoint.getEndpointInfo().getBinding().getOperations()
            .forEach(bindingOpInfo -> bindingOpInfo.setProperty(Message.SCHEMA_VALIDATION_TYPE, SchemaValidationType.BOTH));
    }
}
