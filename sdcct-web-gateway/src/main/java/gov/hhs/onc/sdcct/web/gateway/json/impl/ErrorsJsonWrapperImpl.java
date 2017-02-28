package gov.hhs.onc.sdcct.web.gateway.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.web.gateway.json.ErrorJsonWrapper;
import gov.hhs.onc.sdcct.web.gateway.json.ErrorsJsonWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("errorsJsonWrapperImpl")
@JsonTypeName("errors")
@Lazy
@Scope("prototype")
public class ErrorsJsonWrapperImpl implements ErrorsJsonWrapper {
    private List<ErrorJsonWrapper> globalErrors = new ArrayList<>();
    private ListMultimap<String, ErrorJsonWrapper> fieldErrors = ArrayListMultimap.create();

    @Override
    public boolean hasErrors() {
        return this.hasGlobalErrors() || this.hasFieldErrors();
    }

    @Override
    public boolean hasFieldErrors(String fieldName) {
        return this.fieldErrors.containsKey(fieldName);
    }

    @Override
    public boolean hasFieldErrors() {
        return !this.fieldErrors.isEmpty();
    }

    @Nullable
    @Override
    public List<ErrorJsonWrapper> getFieldErrors(String fieldName) {
        return this.fieldErrors.get(fieldName);
    }

    @Nullable
    @Override
    public ListMultimap<String, ErrorJsonWrapper> getFieldErrors() {
        return this.fieldErrors;
    }

    @Override
    public void setFieldErrors(@Nullable ListMultimap<String, ErrorJsonWrapper> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    @Override
    public boolean hasGlobalErrors() {
        return !this.globalErrors.isEmpty();
    }

    @Nullable
    @Override
    public List<ErrorJsonWrapper> getGlobalErrors() {
        return this.globalErrors;
    }

    @Override
    public void setGlobalErrors(@Nullable List<ErrorJsonWrapper> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
