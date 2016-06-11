package gov.hhs.onc.sdcct.data.db.metadata;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import java.util.Map;
import org.apache.commons.collections4.BidiMap;

public interface EntityMetadata extends DbMetadataComponent {
    public PropertyMetadata getIdProperty();

    public void setIdProperty(PropertyMetadata idProp);

    public Class<? extends SdcctEntity> getMappedClass();

    public Map<String, PropertyMetadata> getProperties();

    public BidiMap<Integer, String> getPropertyOrder();

    public String getTableName();
}
