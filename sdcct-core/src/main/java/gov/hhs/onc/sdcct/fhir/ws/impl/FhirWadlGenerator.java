package gov.hhs.onc.sdcct.fhir.ws.impl;

import javax.annotation.Priority;
import javax.ws.rs.container.PreMatching;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxrs.model.wadl.WadlGenerator;

@PreMatching
@Priority(1)
public class FhirWadlGenerator extends WadlGenerator {
    public FhirWadlGenerator(Bus bus) {
        super(bus);
    }
}
