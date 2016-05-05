package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.SearchParamTypeNames;
import java.math.BigDecimal;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(SearchParamTypeNames.QUANTITY)
@Entity(name = "searchParamQuantity")
@Table(name = DbTableNames.SEARCH_PARAM_QUANTITY)
public class QuantitySearchParamImpl extends AbstractTermSearchParam<BigDecimal> implements QuantitySearchParam {
    private final static long serialVersionUID = 0L;

    private String units;

    public QuantitySearchParamImpl(@Nullable SdcctResource resource, String name, @Nullable URI codeSystemUri, @Nullable String codeSystemVersion,
        @Nullable String units, @Nullable String displayName, BigDecimal value) {
        super(SearchParamType.QUANTITY, resource, name, codeSystemUri, codeSystemVersion, displayName, value);

        this.units = units;
    }

    public QuantitySearchParamImpl() {
        super(SearchParamType.QUANTITY);
    }

    @JoinColumns({ @JoinColumn(name = DbColumnNames.RESOURCE_ID, referencedColumnName = DbColumnNames.ID, updatable = false),
        @JoinColumn(name = DbColumnNames.RESOURCE_VERSION, referencedColumnName = DbColumnNames.VERSION, updatable = false) })
    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Override
    public boolean hasUnits() {
        return (this.units != null);
    }

    @Column(name = DbColumnNames.UNITS)
    @Nullable
    @Override
    public String getUnits() {
        return this.units;
    }

    @Override
    public void setUnits(@Nullable String units) {
        this.units = units;
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public BigDecimal getValue() {
        return super.getValue();
    }
}
