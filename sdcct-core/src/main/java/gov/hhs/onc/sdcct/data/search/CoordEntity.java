package gov.hhs.onc.sdcct.data.search;

import org.hibernate.search.spatial.Coordinates;

public interface CoordEntity extends Coordinates {
    public void setLatitude(Double latitude);

    public void setLongitude(Double longitude);
}
