package gov.hhs.onc.sdcct.web.gateway.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.web.gateway.json.ErrorJsonWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("errorJsonWrapperImpl")
@JsonTypeName("error")
@Lazy
@Scope("prototype")
public class ErrorJsonWrapperImpl implements ErrorJsonWrapper {
    private List<String> msgs = new ArrayList<>();

    public ErrorJsonWrapperImpl() {
        this(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public ErrorJsonWrapperImpl(Throwable error) {
        this(error.getMessage(), Stream.of(ExceptionUtils.getRootCauseStackTrace(error)).collect(Collectors.joining(StringUtils.LF)));
    }

    public ErrorJsonWrapperImpl(String ... msgs) {
        this(Stream.of(msgs).collect(Collectors.toList()));
    }

    public ErrorJsonWrapperImpl(List<String> msgs) {
        this.msgs = msgs;
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
    public void setMessages(List<String> msgs) {
        this.msgs = msgs;
    }
}
