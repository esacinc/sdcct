package gov.hhs.onc.sdcct.data.search;

import java.net.URI;
import javax.annotation.Nullable;

public interface CodeSearchParam<T> extends SearchParam<T> {
    public boolean hasCodeSystemUri();

    @Nullable
    public URI getCodeSystemUri();

    public void setCodeSystemUri(@Nullable URI codeSystemUri);
}
