package gov.hhs.onc.sdcct.data.db.metadata.impl;

import gov.hhs.onc.sdcct.data.db.metadata.PropertyMetadata;
import javax.annotation.Nullable;
import org.hibernate.type.Type;

public class PropertyMetadataImpl extends AbstractDbMetadataComponent implements PropertyMetadata {
    private String columnName;
    private Type type;
    private String edgeNgramFieldName;
    private String lowercaseFieldName;
    private String ngramFieldName;
    private String phoneticFieldName;

    public PropertyMetadataImpl(String name, boolean indexed, String columnName, Type type) {
        super(name, indexed);

        this.columnName = columnName;
        this.type = type;
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }

    @Nullable
    @Override
    public String getEdgeNgramFieldName() {
        return this.edgeNgramFieldName;
    }

    @Override
    public void setEdgeNgramFieldName(@Nullable String edgeNgramFieldName) {
        this.edgeNgramFieldName = edgeNgramFieldName;
    }

    @Nullable
    @Override
    public String getLowercaseFieldName() {
        return this.lowercaseFieldName;
    }

    @Override
    public void setLowercaseFieldName(@Nullable String lowercaseFieldName) {
        this.lowercaseFieldName = lowercaseFieldName;
    }

    @Nullable
    @Override
    public String getNgramFieldName() {
        return this.ngramFieldName;
    }

    @Override
    public void setNgramFieldName(@Nullable String ngramFieldName) {
        this.ngramFieldName = ngramFieldName;
    }

    @Nullable
    @Override
    public String getPhoneticFieldName() {
        return this.phoneticFieldName;
    }

    @Override
    public void setPhoneticFieldName(@Nullable String phoneticFieldName) {
        this.phoneticFieldName = phoneticFieldName;
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
