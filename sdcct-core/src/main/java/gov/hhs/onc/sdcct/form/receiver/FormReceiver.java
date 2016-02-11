package gov.hhs.onc.sdcct.form.receiver;

import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.rfd.AnyXMLContentType;

import javax.annotation.Nullable;

public interface FormReceiver extends FormService {
    @Nullable
    public String submitForm(AnyXMLContentType body) throws Exception;
}
