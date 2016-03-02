package gov.hhs.onc.sdcct.form.archiver.impl;

import gov.hhs.onc.sdcct.form.archiver.FormArchiver;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.rfd.AnyXMLContentType;
import javax.annotation.Nullable;
import org.springframework.stereotype.Component;

// TODO: implement
@Component("formArchiverImpl")
public class FormArchiverImpl extends AbstractFormService implements FormArchiver {
    @Nullable
    @Override
    public String archiveForm(AnyXMLContentType body) throws Exception {
        return "Archived form....";
    }
}
