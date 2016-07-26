package gov.hhs.onc.sdcct.validate.schema;

import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.validate.schema.impl.MsvSchema;
import java.util.Map;

public interface MsvSchemaBuilder {
    public MsvSchema build(ContentPathBuilder contentPathBuilder, String id, String name, Map<String, ResourceSource> schemaSrcs) throws Exception;
}
