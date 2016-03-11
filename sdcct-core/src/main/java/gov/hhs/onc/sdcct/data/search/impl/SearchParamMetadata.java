package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractMetadataComponent;
import gov.hhs.onc.sdcct.data.search.SearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.annotation.Nullable;

public class SearchParamMetadata extends AbstractMetadataComponent {
    private String propName;
    private Class<? extends SearchParam<?>> joinClass;
    private SearchParamType type;

    public SearchParamMetadata(String name, boolean indexed, String propName, @Nullable Class<? extends SearchParam<?>> joinClass, SearchParamType type) {
        super(name, indexed);

        this.propName = propName;
        this.joinClass = joinClass;
        this.type = type;
    }

    public boolean hasJoinClass() {
        return (this.joinClass != null);
    }

    @Nullable
    public Class<? extends SearchParam<?>> getJoinClass() {
        return this.joinClass;
    }

    public String getPropertyName() {
        return this.propName;
    }

    public SearchParamType getType() {
        return this.type;
    }
}
