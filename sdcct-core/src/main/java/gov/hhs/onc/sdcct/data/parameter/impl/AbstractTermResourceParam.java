package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.TermResourceParam;
import java.io.Serializable;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractTermResourceParam<T extends Serializable> extends AbstractResourceParam<T> implements TermResourceParam<T> {
    protected URI codeSystemUri;
    protected String codeSystemVersion;
    protected String displayName;

    private final static long serialVersionUID = 0L;

    protected AbstractTermResourceParam(@Nullable SdcctResource resource, ResourceParamType type, Boolean meta, String name, @Nullable URI codeSystemUri,
        @Nullable String codeSystemVersion, @Nullable String displayName, T value) {
        super(resource, type, meta, name, value);

        this.codeSystemUri = codeSystemUri;
        this.codeSystemVersion = codeSystemVersion;
        this.displayName = displayName;
    }

    protected AbstractTermResourceParam(ResourceParamType type) {
        super(type);
    }

    @Override
    public boolean hasCodeSystemUri() {
        return (this.codeSystemUri != null);
    }

    @Column(name = DbColumnNames.CODE_SYSTEM_URI)
    @Nullable
    @Override
    public URI getCodeSystemUri() {
        return this.codeSystemUri;
    }

    @Override
    public void setCodeSystemUri(@Nullable URI codeSystemUri) {
        this.codeSystemUri = codeSystemUri;
    }

    @Override
    public boolean hasCodeSystemVersion() {
        return (this.codeSystemVersion != null);
    }

    @Column(name = DbColumnNames.CODE_SYSTEM_VERSION)
    @Nullable
    @Override
    public String getCodeSystemVersion() {
        return this.codeSystemVersion;
    }

    @Override
    public void setCodeSystemVersion(@Nullable String codeSystemVersion) {
        this.codeSystemVersion = codeSystemVersion;
    }

    @Override
    public boolean hasDisplayName() {
        return (this.displayName != null);
    }

    @Column(name = DbColumnNames.DISPLAY_NAME)
    @Nullable
    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }
}
