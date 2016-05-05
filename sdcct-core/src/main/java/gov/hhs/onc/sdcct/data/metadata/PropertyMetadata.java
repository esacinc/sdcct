package gov.hhs.onc.sdcct.data.metadata;

import javax.annotation.Nullable;
import org.hibernate.type.Type;

public interface PropertyMetadata extends DbMetadataComponent {
    public String getColumnName();

    @Nullable
    public String getEdgeNgramFieldName();

    public void setEdgeNgramFieldName(@Nullable String edgeNgramFieldName);

    @Nullable
    public String getLowercaseFieldName();

    public void setLowercaseFieldName(@Nullable String lowercaseFieldName);

    @Nullable
    public String getNgramFieldName();

    public void setNgramFieldName(@Nullable String ngramFieldName);

    @Nullable
    public String getPhoneticFieldName();

    public void setPhoneticFieldName(@Nullable String phoneticFieldName);

    public Type getType();
}
