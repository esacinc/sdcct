package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import gov.hhs.onc.sdcct.data.search.impl.CoordSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.DateSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.NumberSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.QuantitySearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.StringSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.TokenSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.UriSearchParamImpl;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@MappedSuperclass
public abstract class AbstractResourceEntity extends AbstractSdcctEntity implements ResourceEntity {
    protected String content;
    protected Map<String, CoordSearchParam> coordSearchParams = new HashMap<>();
    protected Map<String, DateSearchParam> dateSearchParams = new HashMap<>();
    protected String id;
    protected Map<String, NumberSearchParam> numSearchParams = new HashMap<>();
    protected Map<String, QuantitySearchParam> quantitySearchParams = new HashMap<>();
    protected Map<String, StringSearchParam> strSearchParams = new HashMap<>();
    protected Map<String, TokenSearchParam> tokenSearchParams = new HashMap<>();
    protected Map<String, UriSearchParam> uriSearchParams = new HashMap<>();

    @Column(name = DbColumnNames.CONTENT, nullable = false)
    @Field(name = DbColumnNames.CONTENT, store = Store.YES)
    @Lob
    @Override
    @SortableField(forField = DbColumnNames.CONTENT)
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @IndexedEmbedded
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = CoordSearchParamImpl.class)
    @Override
    public Map<String, CoordSearchParam> getCoordSearchParams() {
        return this.coordSearchParams;
    }

    @Override
    public void setCoordSearchParams(Map<String, CoordSearchParam> coordSearchParams) {
        this.coordSearchParams.clear();
        this.coordSearchParams.putAll(coordSearchParams);
    }

    @IndexedEmbedded
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = DateSearchParamImpl.class)
    @Override
    public Map<String, DateSearchParam> getDateSearchParams() {
        return this.dateSearchParams;
    }

    @Override
    public void setDateSearchParams(Map<String, DateSearchParam> dateSearchParams) {
        this.dateSearchParams.clear();
        this.dateSearchParams.putAll(dateSearchParams);
    }

    @Column(name = DbColumnNames.ENTITY_ID)
    @DocumentId(name = DbColumnNames.ENTITY_ID)
    @GeneratedValue(generator = DbSequenceNames.RESOURCE_ENTITY_ID, strategy = GenerationType.SEQUENCE)
    @Id
    @Nullable
    @Override
    @SequenceGenerator(allocationSize = 1, name = DbSequenceNames.RESOURCE_ENTITY_ID, sequenceName = DbSequenceNames.RESOURCE_ENTITY_ID)
    @SortableField(forField = DbColumnNames.ENTITY_ID)
    public Long getEntityId() {
        return super.getEntityId();
    }

    @Column(name = DbColumnNames.ID, nullable = false)
    @Field(name = DbColumnNames.ID, store = Store.YES)
    @NaturalId
    @Override
    @SortableField(forField = DbColumnNames.ID)
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @IndexedEmbedded
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = NumberSearchParamImpl.class)
    @Override
    public Map<String, NumberSearchParam> getNumberSearchParams() {
        return this.numSearchParams;
    }

    @Override
    public void setNumberSearchParams(Map<String, NumberSearchParam> numSearchParams) {
        this.numSearchParams.clear();
        this.numSearchParams.putAll(numSearchParams);
    }

    @IndexedEmbedded
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = QuantitySearchParamImpl.class)
    @Override
    public Map<String, QuantitySearchParam> getQuantitySearchParams() {
        return this.quantitySearchParams;
    }

    @Override
    public void setQuantitySearchParams(Map<String, QuantitySearchParam> quantitySearchParams) {
        this.quantitySearchParams.clear();
        this.quantitySearchParams.putAll(quantitySearchParams);
    }

    @IndexedEmbedded
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = StringSearchParamImpl.class)
    @Override
    public Map<String, StringSearchParam> getStringSearchParams() {
        return this.strSearchParams;
    }

    @Override
    public void setStringSearchParams(Map<String, StringSearchParam> strSearchParams) {
        this.strSearchParams.clear();
        this.strSearchParams.putAll(strSearchParams);
    }

    @IndexedEmbedded
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = TokenSearchParamImpl.class)
    @Override
    public Map<String, TokenSearchParam> getTokenSearchParams() {
        return this.tokenSearchParams;
    }

    @Override
    public void setTokenSearchParams(Map<String, TokenSearchParam> tokenSearchParams) {
        this.tokenSearchParams.clear();
        this.tokenSearchParams.putAll(tokenSearchParams);
    }

    @IndexedEmbedded
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = UriSearchParamImpl.class)
    @Override
    public Map<String, UriSearchParam> getUriSearchParams() {
        return this.uriSearchParams;
    }

    @Override
    public void setUriSearchParams(Map<String, UriSearchParam> uriSearchParams) {
        this.uriSearchParams.clear();
        this.uriSearchParams.putAll(uriSearchParams);
    }
}
