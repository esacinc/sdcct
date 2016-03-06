package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.search.impl.SearchParamMetadata;
import java.util.Map;
import java.util.TreeMap;

public class EntityMetadata extends AbstractMetadataComponent {
    private Class<? extends SdcctEntity> mappedClass;
    private String tableName;
    private Map<String, PropertyMetadata> props = new TreeMap<>();
    private Map<String, SearchParamMetadata> searchParams = new TreeMap<>();

    public EntityMetadata(String name, boolean indexed, Class<? extends SdcctEntity> mappedClass, String tableName) {
        super(name, indexed);

        this.mappedClass = mappedClass;
        this.tableName = tableName;
    }

    public Class<? extends SdcctEntity> getMappedClass() {
        return this.mappedClass;
    }

    public Map<String, PropertyMetadata> getProperties() {
        return this.props;
    }

    public Map<String, SearchParamMetadata> getSearchParams() {
        return this.searchParams;
    }

    public String getTableName() {
        return this.tableName;
    }
}
