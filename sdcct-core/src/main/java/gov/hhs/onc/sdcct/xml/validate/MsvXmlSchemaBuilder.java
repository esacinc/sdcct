package gov.hhs.onc.sdcct.xml.validate;

import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.xml.validate.impl.MsvXmlSchemaImpl;
import java.util.Map;

public interface MsvXmlSchemaBuilder {
    public MsvXmlSchemaImpl build(String id, String name, Map<String, ResourceSource> schemaSrcs) throws Exception;
}
