package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.ResourceType;
import gov.hhs.onc.sdcct.data.db.DbAnalyzerNames;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbFieldNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamDef;
import gov.hhs.onc.sdcct.data.search.SearchParamNames;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import java.util.Map;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;

@MappedSuperclass
public abstract class AbstractFhirResource extends AbstractResourceEntity implements FhirResource {
    protected String text;

    protected AbstractFhirResource(ResourceType type) {
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

    @Override
    @SearchParamDef(name = SearchParamNames.LAST_UPDATED, type = SearchParamType.DATE)
    @Transient
    public Map<String, DateSearchParam> getDateSearchParams() {
        return super.getDateSearchParams();
    }

    @Override
    public boolean hasText() {
        return (this.text != null);
    }

    @Column(name = DbColumnNames.TEXT)
    @Fields({ @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.EDGE_NGRAM), boost = @Boost(0.75F), name = DbFieldNames.TEXT_EDGE_NGRAM),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.LOWERCASE), name = DbFieldNames.TEXT_LOWERCASE),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.NGRAM), boost = @Boost(0.5F), name = DbFieldNames.TEXT_NGRAM),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.PHONETIC), boost = @Boost(0.25F), name = DbFieldNames.TEXT_PHONETIC) })
    @Lob
    @Nullable
    @Override
    @SearchParamDef(name = SearchParamNames.TEXT)
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(@Nullable String text) {
        this.text = text;
    }

    @Override
    @SearchParamDef(name = SearchParamNames.SECURITY, type = SearchParamType.TOKEN)
    @SearchParamDef(name = SearchParamNames.TAG, type = SearchParamType.TOKEN)
    @Transient
    public Map<String, TokenSearchParam> getTokenSearchParams() {
        return super.getTokenSearchParams();
    }

    @Override
    @SearchParamDef(name = SearchParamNames.PROFILE, type = SearchParamType.URI)
    @Transient
    public Map<String, UriSearchParam> getUriSearchParams() {
        return super.getUriSearchParams();
    }
}
