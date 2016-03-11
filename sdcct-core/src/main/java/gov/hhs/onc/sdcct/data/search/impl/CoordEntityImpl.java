package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.search.CoordEntity;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CoordEntityImpl implements CoordEntity {
    private Double latitude;
    private Double longitude;

    public CoordEntityImpl(Double latitude, Double longitude) {
        this();

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CoordEntityImpl() {
    }

    @Column(name = DbColumnNames.LATITUDE, nullable = false)
    @Override
    public Double getLatitude() {
        return this.latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Column(name = DbColumnNames.LONGITUDE, nullable = false)
    @Override
    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
