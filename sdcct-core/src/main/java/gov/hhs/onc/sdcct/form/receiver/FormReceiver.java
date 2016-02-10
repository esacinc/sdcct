package gov.hhs.onc.sdcct.form.receiver;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.FormService;
import javax.annotation.Nullable;

public interface FormReceiver extends FormService {
    @Nullable
    public String submitForm(Form form) throws Exception;
}
