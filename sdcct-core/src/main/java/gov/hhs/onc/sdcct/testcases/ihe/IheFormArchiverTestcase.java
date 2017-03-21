package gov.hhs.onc.sdcct.testcases.ihe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.rfd.ArchiveFormResponseType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.testcases.ihe.impl.IheFormArchiverTestcaseImpl;
import java.util.List;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(IheFormArchiverTestcaseImpl.class) })
public interface IheFormArchiverTestcase extends IheTestcase {
    public boolean hasForms();

    @JsonProperty
    @Nullable
    public List<RfdForm> getForms();

    public void setForms(@Nullable List<RfdForm> forms);

    public boolean hasResponse();

    @JsonProperty
    @Nullable
    public ArchiveFormResponseType getResponse();

    public void setResponse(@Nullable ArchiveFormResponseType response);
}
