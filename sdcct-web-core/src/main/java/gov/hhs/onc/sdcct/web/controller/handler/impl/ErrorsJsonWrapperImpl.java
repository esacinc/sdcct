package gov.hhs.onc.sdcct.web.controller.handler.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.web.controller.handler.ErrorJsonWrapper;
import gov.hhs.onc.sdcct.web.controller.handler.ErrorsJsonWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

@JsonTypeName("errors")
public class ErrorsJsonWrapperImpl implements ErrorsJsonWrapper {
    private ListMultimap<String, ErrorJsonWrapper> fieldErrors = ArrayListMultimap.create();
    private List<ErrorJsonWrapper> globalErrors = new ArrayList<>();

    @Override
    public boolean hasErrors() {
        return (this.hasGlobalErrors() || this.hasFieldErrors());
    }

    @Override
    public ErrorsJsonWrapper addFieldErrors(String fieldName, @Nullable ErrorJsonWrapper ... fieldErrors) {
        if (!ArrayUtils.isEmpty(fieldErrors)) {
            this.fieldErrors.putAll(fieldName, Arrays.asList(fieldErrors));
        }

        return this;
    }

    @Override
    public boolean hasFieldErrors(String fieldName) {
        return this.fieldErrors.containsKey(fieldName);
    }

    @Override
    public boolean hasFieldErrors() {
        return !this.fieldErrors.isEmpty();
    }

    @Override
    public List<ErrorJsonWrapper> getFieldErrors(String fieldName) {
        return this.fieldErrors.get(fieldName);
    }

    @Override
    public ListMultimap<String, ErrorJsonWrapper> getFieldErrors() {
        return this.fieldErrors;
    }

    @Override
    public ErrorsJsonWrapper setFieldErrors(@Nullable ListMultimap<String, ErrorJsonWrapper> fieldErrors) {
        this.fieldErrors.clear();

        if ((fieldErrors != null) && !fieldErrors.isEmpty()) {
            this.fieldErrors.putAll(fieldErrors);
        }

        return this;
    }

    @Override
    public ErrorsJsonWrapper addGlobalErrors(@Nullable ErrorJsonWrapper ... globalErrors) {
        if (!ArrayUtils.isEmpty(globalErrors)) {
            Collections.addAll(this.globalErrors, globalErrors);
        }

        return this;
    }

    @Override
    public boolean hasGlobalErrors() {
        return !this.globalErrors.isEmpty();
    }

    @Override
    public List<ErrorJsonWrapper> getGlobalErrors() {
        return this.globalErrors;
    }

    @Override
    public ErrorsJsonWrapper setGlobalErrors(@Nullable List<ErrorJsonWrapper> globalErrors) {
        this.globalErrors.clear();

        if (!CollectionUtils.isEmpty(globalErrors)) {
            this.globalErrors.addAll(globalErrors);
        }

        return this;
    }
}
