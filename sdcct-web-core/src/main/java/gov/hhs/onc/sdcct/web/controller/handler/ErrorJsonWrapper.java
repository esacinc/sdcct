package gov.hhs.onc.sdcct.web.controller.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.web.controller.handler.impl.ErrorJsonWrapperImpl;
import java.util.List;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(ErrorJsonWrapperImpl.class) })
public interface ErrorJsonWrapper {
    public ErrorJsonWrapper addMessages(@Nullable String ... msgs);

    public boolean hasMessages();

    @JsonProperty
    public List<String> getMessages();

    public ErrorJsonWrapper setMessages(@Nullable List<String> msgs);
}
