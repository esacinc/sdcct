package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamDef;
import gov.hhs.onc.sdcct.data.search.SearchParamNames;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
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
import gov.hhs.onc.sdcct.fhir.FhirForm;
import java.util.Map;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Indexed;

@Cache(region = DbTableNames.FORM_FHIR, usage = CacheConcurrencyStrategy.NONE)
@Cacheable
@Entity(name = "formFhir")
@Indexed(index = DbTableNames.FORM_FHIR)
@Table(name = DbTableNames.FORM_FHIR)
public class FhirFormImpl extends AbstractFhirResource implements FhirForm {
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = CoordSearchParamImpl.class)
    @Override
    public Map<String, CoordSearchParam> getCoordSearchParams() {
        return super.getCoordSearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = DateSearchParamImpl.class)
    @Override
    @SearchParamDef(name = SearchParamNames.DATE, type = SearchParamType.DATE)
    public Map<String, DateSearchParam> getDateSearchParams() {
        return super.getDateSearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = NumberSearchParamImpl.class)
    @Override
    public Map<String, NumberSearchParam> getNumberSearchParams() {
        return super.getNumberSearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = QuantitySearchParamImpl.class)
    @Override
    public
        Map<String, QuantitySearchParam> getQuantitySearchParams() {
        return super.getQuantitySearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = StringSearchParamImpl.class)
    @Override
    @SearchParamDef(name = SearchParamNames.PUBLISHER)
    @SearchParamDef(name = SearchParamNames.TITLE)
    @SearchParamDef(name = SearchParamNames.VERSION)
    public Map<String, StringSearchParam> getStringSearchParams() {
        return super.getStringSearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = TokenSearchParamImpl.class)
    @Override
    @SearchParamDef(name = SearchParamNames.CODE, type = SearchParamType.TOKEN)
    @SearchParamDef(name = SearchParamNames.IDENTIFIER, type = SearchParamType.TOKEN)
    @SearchParamDef(name = SearchParamNames.STATUS, type = SearchParamType.TOKEN)
    public Map<String, TokenSearchParam> getTokenSearchParams() {
        return super.getTokenSearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = UriSearchParamImpl.class)
    @Override
    public Map<String, UriSearchParam> getUriSearchParams() {
        return super.getUriSearchParams();
    }
}
