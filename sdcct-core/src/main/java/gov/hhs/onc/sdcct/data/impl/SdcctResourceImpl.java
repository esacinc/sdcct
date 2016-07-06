package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbAnalyzerNames;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbFieldNames;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.DbQueryNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.parameter.DateResourceParam;
import gov.hhs.onc.sdcct.data.parameter.NumberResourceParam;
import gov.hhs.onc.sdcct.data.parameter.QuantityResourceParam;
import gov.hhs.onc.sdcct.data.parameter.RefResourceParam;
import gov.hhs.onc.sdcct.data.parameter.StringResourceParam;
import gov.hhs.onc.sdcct.data.parameter.TokenResourceParam;
import gov.hhs.onc.sdcct.data.parameter.UriResourceParam;
import gov.hhs.onc.sdcct.data.parameter.impl.DateResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.NumberResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.QuantityResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.RefResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.StringResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.TokenResourceParamImpl;
import gov.hhs.onc.sdcct.data.parameter.impl.UriResourceParamImpl;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.hibernate.annotations.CacheModeType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.SortableField;

@DiscriminatorColumn(name = DbColumnNames.SPEC_TYPE)
@Entity(name = "resource")
@NamedQueries({ @NamedQuery(cacheMode = CacheModeType.IGNORE, name = DbQueryNames.RESOURCE_SELECT_ID_NEW,
    query = ("select (coalesce(max(" + DbPropertyNames.ID + "), 0) + 1) from " + DbTableNames.RESOURCE + " where " + DbPropertyNames.INSTANCE_ID + " = -1")) })
@Table(name = DbTableNames.RESOURCE)
public class SdcctResourceImpl extends AbstractSdcctEntity implements SdcctResource {
    protected SpecificationType specType;
    protected String content;
    protected Set<DateResourceParam> dateParams = new LinkedHashSet<>();
    protected Date deletedTimestamp;
    protected Long entityVersion;
    protected Long id;
    protected Long instanceId;
    protected Date modifiedTimestamp;
    protected Set<NumberResourceParam> numberParams = new LinkedHashSet<>();
    protected Date publishedTimestamp;
    protected Set<QuantityResourceParam> quantityParams = new LinkedHashSet<>();
    protected Set<RefResourceParam> refParams = new LinkedHashSet<>();
    protected Set<StringResourceParam> strParams = new LinkedHashSet<>();
    protected Set<TokenResourceParam> tokenParams = new LinkedHashSet<>();
    protected String text;
    protected String type;
    protected Set<UriResourceParam> uriParams = new LinkedHashSet<>();
    protected Long version;

    private final static long serialVersionUID = 0L;

    public SdcctResourceImpl(SpecificationType specType) {
        this();

        this.specType = specType;
    }

    public SdcctResourceImpl() {
    }

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
    public void addDateParams(DateResourceParam ... dateParams) {
        Stream.of(dateParams).forEach(this.dateParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, referencedColumnName = DbColumnNames.ENTITY_ID)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = DateResourceParamImpl.class)
    @Override
    public Set<DateResourceParam> getDateParams() {
        return this.dateParams;
    }

    public void setDateParams(Set<DateResourceParam> dateParams) {
        this.dateParams = dateParams;
    }

    public boolean hasDeletedTimestamp() {
        return (this.deletedTimestamp != null);
    }

    @Column(name = DbColumnNames.DELETED_TIMESTAMP)
    @Nullable
    @Override
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDeletedTimestamp() {
        return this.deletedTimestamp;
    }

    @Override
    public void setDeletedTimestamp(@Nullable Date deletedTimestamp) {
        this.deletedTimestamp = deletedTimestamp;
    }

    @Column(name = DbColumnNames.ENTITY_ID)
    @DocumentId(name = DbFieldNames.ENTITY_ID)
    @GeneratedValue(generator = DbSequenceNames.RESOURCE_ENTITY_ID, strategy = GenerationType.SEQUENCE)
    @Id
    @Nonnegative
    @Nullable
    @Override
    @SequenceGenerator(allocationSize = 1, name = DbSequenceNames.RESOURCE_ENTITY_ID, sequenceName = DbSequenceNames.RESOURCE_ENTITY_ID)
    @SortableField(forField = DbColumnNames.ENTITY_ID)
    public Long getEntityId() {
        return super.getEntityId();
    }

    @Override
    public boolean hasEntityVersion() {
        return (this.entityVersion != null);
    }

    @Column(name = DbColumnNames.ENTITY_VERSION)
    @Nullable
    @Override
    @Version
    public Long getEntityVersion() {
        return this.entityVersion;
    }

    @Override
    public void setEntityVersion(@Nonnegative @Nullable Long entityVersion) {
        this.entityVersion = entityVersion;
    }

    @Override
    public boolean hasId() {
        return (this.id != null);
    }

    @Column(name = DbColumnNames.ID, nullable = false)
    @Nonnegative
    @Nullable
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(@Nonnegative @Nullable Long id) {
        this.id = id;
    }

    @Override
    public boolean hasInstanceId() {
        return (this.instanceId != null);
    }

    @Column(name = DbColumnNames.INSTANCE_ID, nullable = false)
    @Nullable
    @Override
    public Long getInstanceId() {
        return this.instanceId;
    }

    @Override
    public void setInstanceId(@Nullable Long instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public boolean hasModifiedTimestamp() {
        return (this.modifiedTimestamp != null);
    }

    @Column(name = DbColumnNames.MODIFIED_TIMESTAMP, nullable = false)
    @Nullable
    @Override
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedTimestamp() {
        return this.modifiedTimestamp;
    }

    @Override
    public void setModifiedTimestamp(@Nullable Date modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    @Override
    public void addNumberParams(NumberResourceParam ... numberParams) {
        Stream.of(numberParams).forEach(this.numberParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, referencedColumnName = DbColumnNames.ENTITY_ID)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = NumberResourceParamImpl.class)
    @Override
    public Set<NumberResourceParam> getNumberParams() {
        return this.numberParams;
    }

    public void setNumberParams(Set<NumberResourceParam> numberParams) {
        this.numberParams = numberParams;
    }

    @Override
    public boolean hasPublishedTimestamp() {
        return (this.publishedTimestamp != null);
    }

    @Column(name = DbColumnNames.PUBLISHED_TIMESTAMP, nullable = false)
    @Nullable
    @Override
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPublishedTimestamp() {
        return this.publishedTimestamp;
    }

    @Override
    public void setPublishedTimestamp(@Nullable Date publishedTimestamp) {
        this.publishedTimestamp = publishedTimestamp;
    }

    @Override
    public void addQuantityParams(QuantityResourceParam ... quantityParams) {
        Stream.of(quantityParams).forEach(this.quantityParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, referencedColumnName = DbColumnNames.ENTITY_ID)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = QuantityResourceParamImpl.class)
    @Override
    public Set<QuantityResourceParam> getQuantityParams() {
        return this.quantityParams;
    }

    public void setQuantityParams(Set<QuantityResourceParam> quantityParams) {
        this.quantityParams = quantityParams;
    }

    @Override
    public void addRefParams(RefResourceParam ... refParams) {
        Stream.of(refParams).forEach(this.refParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, referencedColumnName = DbColumnNames.ENTITY_ID)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = RefResourceParamImpl.class)
    @Override
    public Set<RefResourceParam> getRefParams() {
        return this.refParams;
    }

    public void setRefParams(Set<RefResourceParam> refParams) {
        this.refParams = refParams;
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
    public void addStringParams(StringResourceParam ... strParams) {
        Stream.of(strParams).forEach(this.strParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, referencedColumnName = DbColumnNames.ENTITY_ID)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = StringResourceParamImpl.class)
    @Override
    public Set<StringResourceParam> getStringParams() {
        return this.strParams;
    }

    @Override
    public void setStringParams(Set<StringResourceParam> strParams) {
        this.strParams = strParams;
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
    public void addTokenParams(TokenResourceParam ... tokenParams) {
        Stream.of(tokenParams).forEach(this.tokenParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, referencedColumnName = DbColumnNames.ENTITY_ID)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = TokenResourceParamImpl.class)
    @Override
    public Set<TokenResourceParam> getTokenParams() {
        return this.tokenParams;
    }

    public void setTokenParams(Set<TokenResourceParam> tokenParams) {
        this.tokenParams = tokenParams;
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
    public void addUriParams(UriResourceParam ... uriParams) {
        Stream.of(uriParams).forEach(this.uriParams::add);
    }

    @Cascade({ CascadeType.ALL })
    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, referencedColumnName = DbColumnNames.ENTITY_ID)
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = UriResourceParamImpl.class)
    @Override
    public Set<UriResourceParam> getUriParams() {
        return this.uriParams;
    }

    public void setUriParams(Set<UriResourceParam> uriParams) {
        this.uriParams = uriParams;
    }

    @Override
    public boolean hasVersion() {
        return (this.version != null);
    }

    @Column(name = DbColumnNames.VERSION, nullable = false)
    @Nonnegative
    @Nullable
    @Override
    public Long getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(@Nonnegative @Nullable Long version) {
        this.version = version;
    }
}
