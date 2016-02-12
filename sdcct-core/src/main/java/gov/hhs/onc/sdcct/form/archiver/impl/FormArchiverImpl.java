package gov.hhs.onc.sdcct.form.archiver.impl;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.archiver.FormArchiver;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.rfd.AnyXMLContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("formArchiverImpl")
public class FormArchiverImpl extends AbstractFormService implements FormArchiver {
    @Autowired(required = false)
    private List<Form> forms;

    private Map<String, Form> formRepo = new HashMap<>();

    @Nullable
    @Override
    public String archiveForm(AnyXMLContentType body) throws Exception {
        return "Archived form....";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Optional.ofNullable(this.forms).ifPresent(forms -> forms.forEach(form -> this.formRepo.put(form.getId(), form)));
    }
}
