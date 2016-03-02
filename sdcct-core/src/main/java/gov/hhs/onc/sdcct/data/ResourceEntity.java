package gov.hhs.onc.sdcct.data;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import java.util.Map;

public interface ResourceEntity extends IdentifiedBean, SdcctEntity {
    public String getContent();

    public void setContent(String content);

    public Map<String, CoordSearchParam> getCoordSearchParams();

    public void setCoordSearchParams(Map<String, CoordSearchParam> coordSearchParams);

    public Map<String, DateSearchParam> getDateSearchParams();

    public void setDateSearchParams(Map<String, DateSearchParam> dateSearchParams);

    public void setId(String id);

    public Map<String, NumberSearchParam> getNumberSearchParams();

    public void setNumberSearchParams(Map<String, NumberSearchParam> numSearchParams);

    public Map<String, QuantitySearchParam> getQuantitySearchParams();

    public void setQuantitySearchParams(Map<String, QuantitySearchParam> quantitySearchParams);

    public Map<String, StringSearchParam> getStringSearchParams();

    public void setStringSearchParams(Map<String, StringSearchParam> strSearchParams);

    public Map<String, TokenSearchParam> getTokenSearchParams();

    public void setTokenSearchParams(Map<String, TokenSearchParam> tokenSearchParams);

    public Map<String, UriSearchParam> getUriSearchParams();

    public void setUriSearchParams(Map<String, UriSearchParam> uriSearchParams);
}
