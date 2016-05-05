package gov.hhs.onc.sdcct.data;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.RefSearchParam;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import java.util.Set;
import javax.annotation.Nullable;

public interface SdcctResource extends SdcctEntity, SpecifiedBean {
    public String getContent();

    public void setContent(String content);

    public void addDateSearchParams(DateSearchParam ... dateSearchParams);

    public Set<DateSearchParam> getDateSearchParams();

    public void setDateSearchParams(Set<DateSearchParam> dateSearchParams);

    public void addNumberSearchParams(NumberSearchParam ... numberSearchParams);

    public Set<NumberSearchParam> getNumberSearchParams();

    public void setNumberSearchParams(Set<NumberSearchParam> numberSearchParams);

    public void addQuantitySearchParams(QuantitySearchParam ... quantitySearchParams);

    public Set<QuantitySearchParam> getQuantitySearchParams();

    public void setQuantitySearchParams(Set<QuantitySearchParam> quantitySearchParams);

    public void addRefSearchParams(RefSearchParam ... refSearchParams);

    public Set<RefSearchParam> getRefSearchParams();

    public void setRefSearchParams(Set<RefSearchParam> refSearchParams);

    public void setSpecificationType(SpecificationType specType);

    public void addStringSearchParams(StringSearchParam ... strSearchParams);

    public Set<StringSearchParam> getStringSearchParams();

    public void setStringSearchParams(Set<StringSearchParam> strSearchParams);

    public boolean hasText();

    @Nullable
    public String getText();

    public void setText(@Nullable String text);

    public void addTokenSearchParams(TokenSearchParam ... tokenSearchParams);

    public Set<TokenSearchParam> getTokenSearchParams();

    public void setTokenSearchParams(Set<TokenSearchParam> tokenSearchParams);

    public String getType();

    public void setType(String type);

    public void addUriSearchParams(UriSearchParam ... uriSearchParams);

    public Set<UriSearchParam> getUriSearchParams();

    public void setUriSearchParams(Set<UriSearchParam> uriSearchParams);

    public boolean hasVersion();

    @Nullable
    public Long getVersion();

    public void setVersion(@Nullable Long version);
}
