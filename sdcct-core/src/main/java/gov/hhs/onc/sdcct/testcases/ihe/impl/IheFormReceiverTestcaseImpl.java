package gov.hhs.onc.sdcct.testcases.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.rfd.SubmitFormResponseType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

@JsonTypeName("iheFormReceiverTestcase")
public class IheFormReceiverTestcaseImpl extends AbstractIheTestcase implements IheFormReceiverTestcase {
    private List<RfdForm> forms;
    private SubmitFormResponseType response;

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
    public SubmitFormResponseType getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(SubmitFormResponseType response) {
        this.response = response;
    }
}
