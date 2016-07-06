package gov.hhs.onc.sdcct.data;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.data.parameter.DateResourceParam;
import gov.hhs.onc.sdcct.data.parameter.NumberResourceParam;
import gov.hhs.onc.sdcct.data.parameter.QuantityResourceParam;
import gov.hhs.onc.sdcct.data.parameter.RefResourceParam;
import gov.hhs.onc.sdcct.data.parameter.StringResourceParam;
import gov.hhs.onc.sdcct.data.parameter.TokenResourceParam;
import gov.hhs.onc.sdcct.data.parameter.UriResourceParam;
import java.util.Date;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface SdcctResource extends SdcctEntity, SpecifiedBean {
    public String getContent();

    public void setContent(String content);

    public void addDateParams(DateResourceParam ... dateParams);

    public Set<DateResourceParam> getDateParams();

    public void setDateParams(Set<DateResourceParam> dateParams);

    public boolean hasDeletedTimestamp();

    @Nullable
    public Date getDeletedTimestamp();

    public void setDeletedTimestamp(@Nullable Date deletedTimestamp);

    public boolean hasEntityVersion();

    @Nonnegative
    @Nullable
    public Long getEntityVersion();

    public void setEntityVersion(@Nonnegative @Nullable Long entityVersion);

    public boolean hasId();

    @Nonnegative
    @Nullable
    public Long getId();

    public void setId(@Nonnegative @Nullable Long id);

    public boolean hasInstanceId();

    @Nullable
    public Long getInstanceId();

    public void setInstanceId(@Nullable Long instanceId);

    public boolean hasModifiedTimestamp();

    @Nullable
    public Date getModifiedTimestamp();

    public void setModifiedTimestamp(@Nullable Date modifiedTimestamp);

    public void addNumberParams(NumberResourceParam ... numberParams);

    public Set<NumberResourceParam> getNumberParams();

    public void setNumberParams(Set<NumberResourceParam> numberParams);

    public boolean hasPublishedTimestamp();

    @Nullable
    public Date getPublishedTimestamp();

    public void setPublishedTimestamp(@Nullable Date publishedTimestamp);

    public void addQuantityParams(QuantityResourceParam ... quantityParams);

    public Set<QuantityResourceParam> getQuantityParams();

    public void setQuantityParams(Set<QuantityResourceParam> quantityParams);

    public void addRefParams(RefResourceParam ... refParams);

    public Set<RefResourceParam> getRefParams();

    public void setRefParams(Set<RefResourceParam> refParams);

    public void setSpecificationType(SpecificationType specType);

    public void addStringParams(StringResourceParam ... strParams);

    public Set<StringResourceParam> getStringParams();

    public void setStringParams(Set<StringResourceParam> strParams);

    public boolean hasText();

    @Nullable
    public String getText();

    public void setText(@Nullable String text);

    public void addTokenParams(TokenResourceParam ... tokenParams);

    public Set<TokenResourceParam> getTokenParams();

    public void setTokenParams(Set<TokenResourceParam> tokenParams);

    public String getType();

    public void setType(String type);

    public void addUriParams(UriResourceParam ... uriParams);

    public Set<UriResourceParam> getUriParams();

    public void setUriParams(Set<UriResourceParam> uriParams);

    public boolean hasVersion();

    @Nonnegative
    @Nullable
    public Long getVersion();

    public void setVersion(@Nonnegative @Nullable Long version);

}
