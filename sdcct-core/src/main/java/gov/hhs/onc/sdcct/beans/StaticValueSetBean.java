package gov.hhs.onc.sdcct.beans;

import javax.annotation.Nullable;

public interface StaticValueSetBean extends CodeSystemBean, IdentifiedBean, NamedBean, ValueSetBean {
    public String value();

    public String getCodeSystemId();

    public String getCodeSystemName();

    public String getCodeSystemOid();

    public boolean hasCodeSystemVersion();

    @Nullable
    public String getCodeSystemVersion();

    public boolean hasName();

    @Nullable
    public String getName();

    public String getValueSetId();

    public String getValueSetName();

    public String getValueSetOid();

    public boolean hasValueSetVersion();

    @Nullable
    public String getValueSetVersion();
}
