package gov.hhs.onc.sdcct.web.controller.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.web.controller.handler.impl.ErrorsJsonWrapperImpl;
import java.util.List;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(ErrorsJsonWrapperImpl.class) })
public interface ErrorsJsonWrapper {
    public boolean hasErrors();

    public ErrorsJsonWrapper addFieldErrors(String fieldName, @Nullable ErrorJsonWrapper ... fieldErrors);

    public boolean hasFieldErrors(String fieldName);

    public boolean hasFieldErrors();

    public List<ErrorJsonWrapper> getFieldErrors(String fieldName);

    @JsonProperty
    public ListMultimap<String, ErrorJsonWrapper> getFieldErrors();

    public ErrorsJsonWrapper setFieldErrors(@Nullable ListMultimap<String, ErrorJsonWrapper> fieldErrors);

    public ErrorsJsonWrapper addGlobalErrors(@Nullable ErrorJsonWrapper ... globalErrors);

    public boolean hasGlobalErrors();

    @JsonProperty
    public List<ErrorJsonWrapper> getGlobalErrors();

    public ErrorsJsonWrapper setGlobalErrors(@Nullable List<ErrorJsonWrapper> globalErrors);
}
