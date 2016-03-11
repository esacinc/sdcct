package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.ResourceType;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.RefSearchParam;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import gov.hhs.onc.sdcct.data.search.impl.CoordSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.DateSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.NumberSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.QuantitySearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.RefSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.StringSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.TokenSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.UriSearchParamImpl;
import gov.hhs.onc.sdcct.rfd.RfdFormResponse;
import java.util.Map;
import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Indexed;

@Cache(region = DbTableNames.FORM_RESP_RFD, usage = CacheConcurrencyStrategy.NONE)
@Cacheable
@DiscriminatorValue(DbTableNames.FORM_RESP_RFD)
@Entity(name = "formRespRfd")
@Indexed(index = DbTableNames.FORM_RESP_RFD)
@Table(name = DbTableNames.FORM_RESP_RFD)
public class RfdFormResponseImpl extends AbstractRfdResource implements RfdFormResponse {
    public RfdFormResponseImpl() {
        super(ResourceType.FORM_RESP_RFD);
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = CoordSearchParamImpl.class)
    @Override
    public Map<String, CoordSearchParam> getCoordSearchParams() {
        return super.getCoordSearchParams();
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = DateSearchParamImpl.class)
    @Override
    public Map<String, DateSearchParam> getDateSearchParams() {
        return super.getDateSearchParams();
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = NumberSearchParamImpl.class)
    @Override
    public Map<String, NumberSearchParam> getNumberSearchParams() {
        return super.getNumberSearchParams();
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = QuantitySearchParamImpl.class)
    @Override
    public Map<String, QuantitySearchParam> getQuantitySearchParams() {
        return super.getQuantitySearchParams();
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = RefSearchParamImpl.class)
    @Override
    public Map<String, RefSearchParam> getRefSearchParams() {
        return super.getRefSearchParams();
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = StringSearchParamImpl.class)
    @Override
    public Map<String, StringSearchParam> getStringSearchParams() {
        return super.getStringSearchParams();
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = TokenSearchParamImpl.class)
    @Override
    public Map<String, TokenSearchParam> getTokenSearchParams() {
        return super.getTokenSearchParams();
    }

    @Cascade({ CascadeType.ALL })
    @MapKey(name = DbColumnNames.NAME)
    @OneToMany(mappedBy = DbPropertyNames.RESOURCE, orphanRemoval = true, targetEntity = UriSearchParamImpl.class)
    @Override
    public Map<String, UriSearchParam> getUriSearchParams() {
        return super.getUriSearchParams();
    }
}
