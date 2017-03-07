package gov.hhs.onc.sdcct.web.gateway.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ListMultimap;
import java.util.List;
import javax.annotation.Nullable;

public interface ErrorsJsonWrapper {
    public boolean hasErrors();

    public boolean hasFieldErrors(String fieldName);

    public boolean hasFieldErrors();

    @Nullable
    public List<ErrorJsonWrapper> getFieldErrors(String fieldName);

    @JsonProperty("fields")
    public ListMultimap<String, ErrorJsonWrapper> getFieldErrors();

    public void setFieldErrors(ListMultimap<String, ErrorJsonWrapper> fieldErrors);

    public boolean hasGlobalErrors();

    @JsonProperty("global")
    public List<ErrorJsonWrapper> getGlobalErrors();

    public void setGlobalErrors(List<ErrorJsonWrapper> globalErrors);
}
