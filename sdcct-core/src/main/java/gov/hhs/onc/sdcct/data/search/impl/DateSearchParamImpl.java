package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.DateSearchPeriod;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.SearchParamTypeNames;
import javax.annotation.Nullable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Target;

@DiscriminatorValue(SearchParamTypeNames.DATE)
@Entity(name = "searchParamDate")
@Table(name = DbTableNames.SEARCH_PARAM_DATE)
public class DateSearchParamImpl extends AbstractSearchParam<DateSearchPeriod> implements DateSearchParam {
    private final static long serialVersionUID = 0L;

    public DateSearchParamImpl(@Nullable SdcctResource resource, String name, DateSearchPeriod value) {
        super(SearchParamType.DATE, resource, name, value);
    }

    public DateSearchParamImpl() {
        super(SearchParamType.DATE);
    }

    @JoinColumns({ @JoinColumn(name = DbColumnNames.RESOURCE_ID, referencedColumnName = DbColumnNames.ID, updatable = false),
        @JoinColumn(name = DbColumnNames.RESOURCE_VERSION, referencedColumnName = DbColumnNames.VERSION, updatable = false) })
    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Embedded
    @Override
    @Target(DateSearchPeriodImpl.class)
    public DateSearchPeriod getValue() {
        return super.getValue();
    }
}
