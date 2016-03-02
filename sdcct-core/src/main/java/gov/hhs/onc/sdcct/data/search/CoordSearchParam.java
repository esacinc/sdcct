package gov.hhs.onc.sdcct.data.search;

public interface CoordSearchParam extends SearchParam {
    public double getLatitude();

    public void setLatitude(double latitude);

    public double getLongitude();

    public void setLongitude(double longitude);
}
