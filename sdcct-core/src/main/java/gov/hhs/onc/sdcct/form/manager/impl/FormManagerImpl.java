package gov.hhs.onc.sdcct.form.manager.impl;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("formManagerImpl")
public class FormManagerImpl extends AbstractFormService implements FormManager {
    @Autowired(required = false)
    private List<Form> forms;

    private Map<String, Form> formRepo = new HashMap<>();

    @Nullable
    @Override
    public Form retrieveForm(String formId) throws Exception {
        return this.formRepo.get(formId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Optional.ofNullable(this.forms).ifPresent(forms -> forms.forEach(form -> this.formRepo.put(form.getId(), form)));
    }
}
