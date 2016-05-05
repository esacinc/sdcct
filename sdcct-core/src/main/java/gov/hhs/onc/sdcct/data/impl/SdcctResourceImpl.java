package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbAnalyzerNames;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbFieldNames;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.RefSearchParam;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import gov.hhs.onc.sdcct.data.search.impl.DateSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.NumberSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.QuantitySearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.RefSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.StringSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.TokenSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.UriSearchParamImpl;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.SortableField;

@Audited
@AuditTable(DbTableNames.RESOURCE_HISTORY)
@DiscriminatorColumn(name = DbColumnNames.SPEC_TYPE)
@Entity(name = "resource")
@Table(name = DbTableNames.RESOURCE)
public class SdcctResourceImpl extends AbstractSdcctEntity implements SdcctResource {
    protected SpecificationType specType;
    protected String content;
    protected Set<DateSearchParam> dateSearchParams = new LinkedHashSet<>();
    protected Set<NumberSearchParam> numberSearchParams = new LinkedHashSet<>();
    protected Set<QuantitySearchParam> quantitySearchParams = new LinkedHashSet<>();
    protected Set<RefSearchParam> refSearchParams = new LinkedHashSet<>();
    protected Set<StringSearchParam> strSearchParams = new LinkedHashSet<>();
    protected Set<TokenSearchParam> tokenSearchParams = new LinkedHashSet<>();
    protected String text;
    protected String type;
    protected Set<UriSearchParam> uriSearchParams = new LinkedHashSet<>();
    protected Long version;

    private final static long serialVersionUID = 0L;

    @Column(name = DbColumnNames.CONTENT, nullable = false)
    @Fields({ @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.EDGE_NGRAM), boost = @Boost(0.75F), name = DbFieldNames.CONTENT_EDGE_NGRAM),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.LOWERCASE), name = DbFieldNames.CONTENT_LOWERCASE),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.NGRAM), boost = @Boost(0.5F), name = DbFieldNames.CONTENT_NGRAM),
        @Field(analyzer = @Analyzer(definition = DbAnalyzerNames.PHONETIC), boost = @Boost(0.25F), name = DbFieldNames.CONTENT_PHONETIC) })
    @Lob
    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void addDateSearchParams(DateSearchParam ... dateSearchParams) {
        Stream.of(dateSearchParams).forEach(this.dateSearchParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = DateSearchParamImpl.class)
    @Override
    public Set<DateSearchParam> getDateSearchParams() {
        return this.dateSearchParams;
    }

    @Override
    public void setDateSearchParams(Set<DateSearchParam> dateSearchParams) {
        this.dateSearchParams = dateSearchParams;
    }

    @Column(name = DbColumnNames.ID)
    @DocumentId(name = DbColumnNames.ID)
    @GeneratedValue(generator = DbSequenceNames.RESOURCE_ID, strategy = GenerationType.SEQUENCE)
    @Id
    @Nullable
    @Override
    @SequenceGenerator(allocationSize = 1, name = DbSequenceNames.RESOURCE_ID, sequenceName = DbSequenceNames.RESOURCE_ID)
    @SortableField(forField = DbColumnNames.ID)
    public Long getId() {
        return super.getId();
    }

    @Override
    public void addNumberSearchParams(NumberSearchParam ... numberSearchParams) {
        Stream.of(numberSearchParams).forEach(this.numberSearchParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = NumberSearchParamImpl.class)
    @Override
    public Set<NumberSearchParam> getNumberSearchParams() {
        return this.numberSearchParams;
    }

    @Override
    public void setNumberSearchParams(Set<NumberSearchParam> numberSearchParams) {
        this.numberSearchParams = numberSearchParams;
    }

    @Override
    public void addQuantitySearchParams(QuantitySearchParam ... quantitySearchParams) {
        Stream.of(quantitySearchParams).forEach(this.quantitySearchParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = QuantitySearchParamImpl.class)
    @Override
    public Set<QuantitySearchParam> getQuantitySearchParams() {
        return this.quantitySearchParams;
    }

    @Override
    public void setQuantitySearchParams(Set<QuantitySearchParam> quantitySearchParams) {
        this.quantitySearchParams = quantitySearchParams;
    }

    @Override
    public void addRefSearchParams(RefSearchParam ... refSearchParams) {
        Stream.of(refSearchParams).forEach(this.refSearchParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = RefSearchParamImpl.class)
    @Override
    public Set<RefSearchParam> getRefSearchParams() {
        return this.refSearchParams;
    }

    @Override
    public void setRefSearchParams(Set<RefSearchParam> refSearchParams) {
        this.refSearchParams = refSearchParams;
    }

    @Column(insertable = false, name = DbColumnNames.SPEC_TYPE, nullable = false, updatable = false)
    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }

    @Override
    public void setSpecificationType(SpecificationType specType) {
        this.specType = specType;
    }

    @Override
    public void addStringSearchParams(StringSearchParam ... strSearchParams) {
        Stream.of(strSearchParams).forEach(this.strSearchParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = StringSearchParamImpl.class)
    @Override
    public Set<StringSearchParam> getStringSearchParams() {
        return this.strSearchParams;
    }

    @Override
    public void setStringSearchParams(Set<StringSearchParam> strSearchParams) {
        this.strSearchParams = strSearchParams;
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
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(@Nullable String text) {
        this.text = text;
    }

    @Override
    public void addTokenSearchParams(TokenSearchParam ... tokenSearchParams) {
        Stream.of(tokenSearchParams).forEach(this.tokenSearchParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = TokenSearchParamImpl.class)
    @Override
    public Set<TokenSearchParam> getTokenSearchParams() {
        return this.tokenSearchParams;
    }

    @Override
    public void setTokenSearchParams(Set<TokenSearchParam> tokenSearchParams) {
        this.tokenSearchParams = tokenSearchParams;
    }

    @Column(name = DbColumnNames.TYPE, nullable = false, updatable = false)
    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void addUriSearchParams(UriSearchParam ... uriSearchParams) {
        Stream.of(uriSearchParams).forEach(this.uriSearchParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @NotAudited
    @OneToMany(fetch = FetchType.EAGER, mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = UriSearchParamImpl.class)
    @Override
    public Set<UriSearchParam> getUriSearchParams() {
        return this.uriSearchParams;
    }

    @Override
    public void setUriSearchParams(Set<UriSearchParam> uriSearchParams) {
        this.uriSearchParams = uriSearchParams;
    }

    @Override
    public boolean hasVersion() {
        return (this.version != null);
    }

    @Column(name = DbColumnNames.VERSION, nullable = false)
    @NotAudited
    @Nullable
    @Override
    public Long getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(@Nullable Long version) {
        this.version = version;
    }
}
