package gov.hhs.onc.sdcct.form.archiver;

import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.rfd.AnyXMLContentType;

import javax.annotation.Nullable;

public interface FormArchiver extends FormService {
    @Nullable
    public String archiveForm(AnyXMLContentType body) throws Exception;
}
