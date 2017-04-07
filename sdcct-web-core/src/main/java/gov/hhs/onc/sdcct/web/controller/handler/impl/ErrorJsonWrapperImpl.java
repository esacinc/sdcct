package gov.hhs.onc.sdcct.web.controller.handler.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.web.controller.handler.ErrorJsonWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

@JsonTypeName("error")
public class ErrorJsonWrapperImpl implements ErrorJsonWrapper {
    private List<String> msgs = new ArrayList<>();

    public ErrorJsonWrapperImpl(Throwable throwable) {
        this(SdcctExceptionUtils.buildRootCauseStackTrace(throwable));
    }

    public ErrorJsonWrapperImpl(@Nullable String ... msgs) {
        this.addMessages(msgs);
    }

    @Override
    public ErrorJsonWrapper addMessages(@Nullable String ... msgs) {
        if (!ArrayUtils.isEmpty(msgs)) {
            Collections.addAll(this.msgs, msgs);
        }

        return this;
    }

    @Override
    public boolean hasMessages() {
        return !this.msgs.isEmpty();
    }

    @Override
    public List<String> getMessages() {
        return this.msgs;
    }

    @Override
    public ErrorJsonWrapper setMessages(@Nullable List<String> msgs) {
        this.msgs.clear();

        if (!CollectionUtils.isEmpty(msgs)) {
            this.msgs.addAll(msgs);
        }

        return this;
    }
}
