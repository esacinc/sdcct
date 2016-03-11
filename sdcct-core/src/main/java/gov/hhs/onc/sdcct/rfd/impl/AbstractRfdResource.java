package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.ResourceType;
import gov.hhs.onc.sdcct.data.db.DbAnalyzerNames;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbFieldNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.SearchParamDef;
import gov.hhs.onc.sdcct.data.search.SearchParamNames;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;

@MappedSuperclass
public abstract class AbstractRfdResource extends AbstractResourceEntity implements RfdResource {
    protected AbstractRfdResource(ResourceType type) {
        super(type);
    }

    @Column(name = DbColumnNames.CONTENT, nullable = false)
    @Fields({ @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.EDGE_NGRAM), boost = @Boost(0.75F), name = DbFieldNames.CONTENT_EDGE_NGRAM),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.LOWERCASE), name = DbFieldNames.CONTENT_LOWERCASE),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.NGRAM), boost = @Boost(0.5F), name = DbFieldNames.CONTENT_NGRAM),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.PHONETIC), boost = @Boost(0.25F), name = DbFieldNames.CONTENT_PHONETIC) })
    @Lob
    @Override
    @SearchParamDef(name = SearchParamNames.CONTENT)
    public String getContent() {
        return super.getContent();
    }
}
