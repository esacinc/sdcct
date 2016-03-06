package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
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
import gov.hhs.onc.sdcct.rfd.RfdForm;
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

@Cache(region = DbTableNames.FORM_RFD, usage = CacheConcurrencyStrategy.NONE)
@Cacheable
@Entity(name = "formRfd")
@Indexed(index = DbTableNames.FORM_RFD)
@Table(name = DbTableNames.FORM_RFD)
public class RfdFormImpl extends AbstractRfdResource implements RfdForm {
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = CoordSearchParamImpl.class)
    @Override
    public Map<String, CoordSearchParam> getCoordSearchParams() {
        return super.getCoordSearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = DateSearchParamImpl.class)
    @Override
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
    public Map<String, StringSearchParam> getStringSearchParams() {
        return super.getStringSearchParams();
    }

    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = DbPropertyNames.RESOURCE_ENTITY_ID, orphanRemoval = true, targetEntity = TokenSearchParamImpl.class)
    @Override
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
