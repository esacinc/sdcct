package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
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
@Entity(name = "searchParamStr")
@Indexed(index = DbTableNames.SEARCH_PARAM_STR)
@Table(name = DbTableNames.SEARCH_PARAM_STR)
public class StringSearchParamImpl extends AbstractSearchParam implements StringSearchParam {
    private String value;

    public StringSearchParamImpl(@Nullable Long resourceEntityId, String name, String value) {
        super(SearchParamType.STRING, resourceEntityId, name);

        this.value = value;
    }

    public StringSearchParamImpl() {
        super(SearchParamType.STRING);
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Field(name = DbColumnNames.VALUE, store = Store.YES)
    @Override
    @SortableField(forField = DbColumnNames.VALUE)
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
