package gov.hhs.onc.sdcct.testcases.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.rfd.ArchiveFormResponseType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

@JsonTypeName("iheFormArchiverTestcase")
public class IheFormArchiverTestcaseImpl extends AbstractIheTestcase implements IheFormArchiverTestcase {
    private List<RfdForm> forms;
    private ArchiveFormResponseType response;

    @Override
    public boolean hasForms() {
        return !CollectionUtils.isEmpty(this.forms);
    }

    @Nullable
    @Override
    public List<RfdForm> getForms() {
        return this.forms;
    }

    @Override
    public void setForms(@Nullable List<RfdForm> forms) {
        this.forms = forms;
    }

    @Override
    public boolean hasResponse() {
        return this.response != null;
    }

    @Nullable
    @Override
    public ArchiveFormResponseType getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(@Nullable ArchiveFormResponseType response) {
        this.response = response;
    }
}
