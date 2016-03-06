package gov.hhs.onc.sdcct.data.db.impl;

import javax.annotation.Nullable;
import org.hibernate.type.Type;

public class PropertyMetadata extends AbstractMetadataComponent {
    private String columnName;
    private Type type;
    private String edgeNgramFieldName;
    private String lowercaseFieldName;
    private String ngramFieldName;
    private String phoneticFieldName;

    public PropertyMetadata(String name, boolean indexed, String columnName, Type type) {
        super(name, indexed);

        this.columnName = columnName;
        this.type = type;
    }

    public String getColumnName() {
        return this.columnName;
    }

    @Nullable
    public String getEdgeNgramFieldName() {
        return this.edgeNgramFieldName;
    }

    public void setEdgeNgramFieldName(@Nullable String edgeNgramFieldName) {
        this.edgeNgramFieldName = edgeNgramFieldName;
    }

    @Nullable
    public String getLowercaseFieldName() {
        return this.lowercaseFieldName;
    }

    public void setLowercaseFieldName(@Nullable String lowercaseFieldName) {
        this.lowercaseFieldName = lowercaseFieldName;
    }

    @Nullable
    public String getNgramFieldName() {
        return this.ngramFieldName;
    }

    public void setNgramFieldName(@Nullable String ngramFieldName) {
        this.ngramFieldName = ngramFieldName;
    }

    @Nullable
    public String getPhoneticFieldName() {
        return this.phoneticFieldName;
    }

    public void setPhoneticFieldName(@Nullable String phoneticFieldName) {
        this.phoneticFieldName = phoneticFieldName;
    }

    public Type getType() {
        return this.type;
    }
}
