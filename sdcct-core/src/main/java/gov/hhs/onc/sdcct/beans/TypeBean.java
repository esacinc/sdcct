package gov.hhs.onc.sdcct.beans;

import java.net.URI;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public interface TypeBean {
    public String getTypeId();

    public String getTypeName();

    public boolean hasTypePattern();

    @Nullable
    public Pattern getTypePattern();

    public boolean hasTypeUri();

    @Nullable
    public URI getTypeUri();

    public boolean hasTypeVersion();

    @Nullable
    public String getTypeVersion();
}
