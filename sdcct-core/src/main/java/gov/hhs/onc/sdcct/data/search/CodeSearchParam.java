package gov.hhs.onc.sdcct.data.search;

import javax.annotation.Nullable;

public interface CodeSearchParam extends SearchParam {
    public boolean hasCodeSystem();

    @Nullable
    public String getCodeSystem();

    public void setCodeSystem(@Nullable String codeSystem);
}
