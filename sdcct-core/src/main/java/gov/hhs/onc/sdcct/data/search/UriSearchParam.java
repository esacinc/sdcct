package gov.hhs.onc.sdcct.data.search;

import java.net.URI;

public interface UriSearchParam extends SearchParam {
    public URI getValue();

    public void setValue(URI value);
}
