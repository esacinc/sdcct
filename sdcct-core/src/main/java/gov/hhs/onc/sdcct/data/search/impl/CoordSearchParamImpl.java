package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "searchParamCoord")
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
    @Override
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Column(name = DbColumnNames.LONGITUDE, nullable = false)
    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
