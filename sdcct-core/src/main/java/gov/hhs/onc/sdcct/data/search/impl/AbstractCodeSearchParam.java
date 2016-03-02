package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.search.CodeSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@MappedSuperclass
public abstract class AbstractCodeSearchParam extends AbstractSearchParam implements CodeSearchParam {
    protected String codeSystem;

    protected AbstractCodeSearchParam(SearchParamType type, @Nullable Long resourceEntityId, String name, String codeSystem) {
        super(type, resourceEntityId, name);

        this.codeSystem = codeSystem;
    }

    protected AbstractCodeSearchParam(SearchParamType type) {
        super(type);
    }

    public boolean hasCodeSystem() {
        return (this.codeSystem != null);
    }

    @Column(name = DbColumnNames.CODE_SYSTEM)
    @Field(name = DbColumnNames.CODE_SYSTEM, store = Store.YES)
    @Nullable
    @Override
    @SortableField(forField = DbColumnNames.CODE_SYSTEM)
    public String getCodeSystem() {
        return this.codeSystem;
    }

    @Override
    public void setCodeSystem(@Nullable String codeSystem) {
        this.codeSystem = codeSystem;
    }
}
