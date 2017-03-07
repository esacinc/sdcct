package gov.hhs.onc.sdcct.web.gateway.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public interface ErrorJsonWrapper {
    public boolean hasMessages();

    @JsonProperty("msgs")
    public List<String> getMessages();

    public void setMessages(List<String> msgs);
}
