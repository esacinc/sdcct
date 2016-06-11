package gov.hhs.onc.sdcct.data.db.metadata.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.metadata.EntityMetadata;
import gov.hhs.onc.sdcct.data.db.metadata.PropertyMetadata;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

public class EntityMetadataImpl extends AbstractDbMetadataComponent implements EntityMetadata {
    private Class<? extends SdcctEntity> mappedClass;
    private String tableName;
    private PropertyMetadata idProp;
    private Map<String, PropertyMetadata> props = new TreeMap<>();
    private BidiMap<Integer, String> propOrder = new TreeBidiMap<>();

    public EntityMetadataImpl(String name, boolean indexed, Class<? extends SdcctEntity> mappedClass, String tableName) {
        super(name, indexed);

        this.mappedClass = mappedClass;
        this.tableName = tableName;
    }

    @Override
    public PropertyMetadata getIdProperty() {
        return this.idProp;
    }

    @Override
    public void setIdProperty(PropertyMetadata idProp) {
        this.idProp = idProp;
    }

    @Override
    public Class<? extends SdcctEntity> getMappedClass() {
        return this.mappedClass;
    }

    @Override
    public Map<String, PropertyMetadata> getProperties() {
        return this.props;
    }

    @Override
    public BidiMap<Integer, String> getPropertyOrder() {
        return this.propOrder;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }
}
