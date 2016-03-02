package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
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
@Entity(name = "searchParamToken")
@Indexed(index = DbTableNames.SEARCH_PARAM_TOKEN)
@Table(name = DbTableNames.SEARCH_PARAM_TOKEN)
public class TokenSearchParamImpl extends AbstractCodeSearchParam implements TokenSearchParam {
    private String value;

    public TokenSearchParamImpl(@Nullable Long resourceEntityId, String name, String codeSystem, String value) {
        super(SearchParamType.TOKEN, resourceEntityId, name, codeSystem);

        this.value = value;
    }

    public TokenSearchParamImpl() {
        super(SearchParamType.TOKEN);
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
