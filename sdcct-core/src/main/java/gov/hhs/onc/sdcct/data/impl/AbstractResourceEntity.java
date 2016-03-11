package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.ResourceType;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.RefSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamDef;
import gov.hhs.onc.sdcct.data.search.SearchParamNames;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.SortableField;

@DiscriminatorColumn(name = DbColumnNames.TYPE)
@Entity(name = "resource")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractResourceEntity extends AbstractSdcctEntity implements ResourceEntity {
    protected String content;
    protected Map<String, CoordSearchParam> coordSearchParams = new HashMap<>();
    protected Map<String, DateSearchParam> dateSearchParams = new HashMap<>();
    protected String id;
    protected Map<String, NumberSearchParam> numSearchParams = new HashMap<>();
    protected Map<String, QuantitySearchParam> quantitySearchParams = new HashMap<>();
    protected Map<String, RefSearchParam> refSearchParams = new HashMap<>();
    protected Map<String, StringSearchParam> strSearchParams = new HashMap<>();
    protected Map<String, TokenSearchParam> tokenSearchParams = new HashMap<>();
    protected ResourceType type;
    protected Map<String, UriSearchParam> uriSearchParams = new HashMap<>();

    protected AbstractResourceEntity(ResourceType type) {
        this.type = type;
    }

    @Override
    @Transient
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void addCoordSearchParams(CoordSearchParam ... coordSearchParams) {
        Stream.of(coordSearchParams).forEach(coordSearchParam -> this.coordSearchParams.put(coordSearchParam.getName(), coordSearchParam));
    }

    @Override
    @Transient
    public Map<String, CoordSearchParam> getCoordSearchParams() {
        return this.coordSearchParams;
    }

    @Override
    public void setCoordSearchParams(Map<String, CoordSearchParam> coordSearchParams) {
        this.coordSearchParams.clear();
        this.coordSearchParams.putAll(coordSearchParams);
    }

    @Override
    public void addDateSearchParams(DateSearchParam ... dateSearchParams) {
        Stream.of(dateSearchParams).forEach(dateSearchParam -> this.dateSearchParams.put(dateSearchParam.getName(), dateSearchParam));
    }

    @Override
    @Transient
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
    @NaturalId
    @Override
    @SearchParamDef(name = SearchParamNames.ID)
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void addNumberSearchParams(NumberSearchParam ... numSearchParams) {
        Stream.of(numSearchParams).forEach(numSearchParam -> this.numSearchParams.put(numSearchParam.getName(), numSearchParam));
    }

    @Override
    @Transient
    public Map<String, NumberSearchParam> getNumberSearchParams() {
        return this.numSearchParams;
    }

    @Override
    public void setNumberSearchParams(Map<String, NumberSearchParam> numSearchParams) {
        this.numSearchParams.clear();
        this.numSearchParams.putAll(numSearchParams);
    }

    @Override
    public void addQuantitySearchParams(QuantitySearchParam ... quantitySearchParams) {
        Stream.of(quantitySearchParams).forEach(quantitySearchParam -> this.quantitySearchParams.put(quantitySearchParam.getName(), quantitySearchParam));
    }

    @Override
    @Transient
    public Map<String, QuantitySearchParam> getQuantitySearchParams() {
        return this.quantitySearchParams;
    }

    @Override
    public void setQuantitySearchParams(Map<String, QuantitySearchParam> quantitySearchParams) {
        this.quantitySearchParams.clear();
        this.quantitySearchParams.putAll(quantitySearchParams);
    }

    @Override
    public void addRefSearchParams(RefSearchParam ... refSearchParams) {
        Stream.of(refSearchParams).forEach(refSearchParam -> this.refSearchParams.put(refSearchParam.getName(), refSearchParam));
    }

    @Override
    @Transient
    public Map<String, RefSearchParam> getRefSearchParams() {
        return this.refSearchParams;
    }

    @Override
    public void setRefSearchParams(Map<String, RefSearchParam> refSearchParams) {
        this.refSearchParams.clear();
        this.refSearchParams.putAll(refSearchParams);
    }

    @Override
    public void addStringSearchParams(StringSearchParam ... strSearchParams) {
        Stream.of(strSearchParams).forEach(strSearchParam -> this.strSearchParams.put(strSearchParam.getName(), strSearchParam));
    }

    @Override
    @Transient
    public Map<String, StringSearchParam> getStringSearchParams() {
        return this.strSearchParams;
    }

    @Override
    public void setStringSearchParams(Map<String, StringSearchParam> strSearchParams) {
        this.strSearchParams.clear();
        this.strSearchParams.putAll(strSearchParams);
    }

    @Override
    public void addTokenSearchParams(TokenSearchParam ... tokenSearchParams) {
        Stream.of(tokenSearchParams).forEach(tokenSearchParam -> this.tokenSearchParams.put(tokenSearchParam.getName(), tokenSearchParam));
    }

    @Override
    @Transient
    public Map<String, TokenSearchParam> getTokenSearchParams() {
        return this.tokenSearchParams;
    }

    @Override
    public void setTokenSearchParams(Map<String, TokenSearchParam> tokenSearchParams) {
        this.tokenSearchParams.clear();
        this.tokenSearchParams.putAll(tokenSearchParams);
    }

    @Column(name = DbColumnNames.TYPE, nullable = false, updatable = false)
    @Override
    public ResourceType getType() {
        return this.type;
    }

    @Override
    public void setType(ResourceType type) {
        this.type = type;
    }

    @Override
    public void addUriSearchParams(UriSearchParam ... uriSearchParams) {
        Stream.of(uriSearchParams).forEach(uriSearchParam -> this.uriSearchParams.put(uriSearchParam.getName(), uriSearchParam));
    }

    @Override
    @Transient
    public Map<String, UriSearchParam> getUriSearchParams() {
        return this.uriSearchParams;
    }

    @Override
    public void setUriSearchParams(Map<String, UriSearchParam> uriSearchParams) {
        this.uriSearchParams.clear();
        this.uriSearchParams.putAll(uriSearchParams);
    }
}
