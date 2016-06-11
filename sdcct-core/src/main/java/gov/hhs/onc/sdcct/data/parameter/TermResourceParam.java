package gov.hhs.onc.sdcct.data.parameter;

import gov.hhs.onc.sdcct.beans.CodeSystemBean;
import java.io.Serializable;
import java.net.URI;
import javax.annotation.Nullable;

public interface TermResourceParam<T extends Serializable> extends CodeSystemBean, ResourceParam<T> {
    public boolean hasCodeSystemUri();

    @Nullable
    @Override
    public URI getCodeSystemUri();

    public void setCodeSystemUri(@Nullable URI codeSystemUri);

    public boolean hasCodeSystemVersion();

    @Nullable
    public String getCodeSystemVersion();

    public void setCodeSystemVersion(@Nullable String codeSystemVersion);

    public boolean hasDisplayName();

    @Nullable
    public String getDisplayName();

    public void setDisplayName(@Nullable String displayName);
}
