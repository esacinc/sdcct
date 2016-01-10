package gov.hhs.onc.sdcct.form.manager;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.FormService;
import javax.annotation.Nullable;

public interface FormManager extends FormService {
    @Nullable
    public Form retrieveForm(String formId) throws Exception;
}
