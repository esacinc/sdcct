package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@Analyzer(impl = StandardAnalyzer.class)
@Embeddable
@Entity(name = "searchParamUri")
@Indexed(index = DbTableNames.SEARCH_PARAM_URI)
@Table(name = DbTableNames.SEARCH_PARAM_URI)
public class UriSearchParamImpl extends AbstractSearchParam implements UriSearchParam {
    private URI value;

    public UriSearchParamImpl(String name, @Nullable Long resourceEntityId, URI value) {
        super(SearchParamType.URI, resourceEntityId, name);

        this.value = value;
    }

    public UriSearchParamImpl() {
        super(SearchParamType.URI);
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Field(name = DbColumnNames.VALUE, store = Store.YES)
    @Override
    @SortableField(forField = DbColumnNames.VALUE)
    public URI getValue() {
        return this.value;
    }

    @Override
    public void setValue(URI value) {
        this.value = value;
    }
}
