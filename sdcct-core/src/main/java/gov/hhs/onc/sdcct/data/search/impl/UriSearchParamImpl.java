package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.SearchParamTypeNames;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(SearchParamTypeNames.URI)
@Entity(name = "searchParamUri")
@Table(name = DbTableNames.SEARCH_PARAM_URI)
public class UriSearchParamImpl extends AbstractSearchParam<URI> implements UriSearchParam {
    private final static long serialVersionUID = 0L;

    public UriSearchParamImpl(@Nullable SdcctResource resource, String name, URI value) {
        super(SearchParamType.URI, resource, name, value);
    }

    public UriSearchParamImpl() {
        super(SearchParamType.URI);
    }

    @JoinColumns({ @JoinColumn(name = DbColumnNames.RESOURCE_ID, referencedColumnName = DbColumnNames.ID, updatable = false),
        @JoinColumn(name = DbColumnNames.RESOURCE_VERSION, referencedColumnName = DbColumnNames.VERSION, updatable = false) })
    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public URI getValue() {
        return super.getValue();
    }
}
