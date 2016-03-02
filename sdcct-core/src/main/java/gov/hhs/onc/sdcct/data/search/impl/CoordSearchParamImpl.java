package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.Store;

@Analyzer(impl = StandardAnalyzer.class)
@Embeddable
@Entity(name = "searchParamCoord")
@Indexed(index = DbTableNames.SEARCH_PARAM_COORD)
@Spatial
@Table(name = DbTableNames.SEARCH_PARAM_COORD)
public class CoordSearchParamImpl extends AbstractSearchParam implements CoordSearchParam {
    private double latitude;
    private double longitude;

    public CoordSearchParamImpl(@Nullable Long resourceEntityId, String name, double latitude, double longitude) {
        super(SearchParamType.COORD, resourceEntityId, name);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CoordSearchParamImpl() {
        super(SearchParamType.COORD);
    }

    @Column(name = DbColumnNames.LATITUDE, nullable = false)
    @Field(name = DbColumnNames.LATITUDE, store = Store.YES)
    @Latitude
    @Override
    @SortableField(forField = DbColumnNames.LATITUDE)
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Column(name = DbColumnNames.LONGITUDE, nullable = false)
    @Field(name = DbColumnNames.LONGITUDE, store = Store.YES)
    @Longitude
    @Override
    @SortableField(forField = DbColumnNames.LONGITUDE)
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
