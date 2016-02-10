package gov.hhs.onc.sdcct.form.receiver.impl;

import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.form.receiver.FormReceiver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("formReceiverImpl")
public class FormReceiverImpl extends AbstractFormService implements FormReceiver {
    @Autowired(required = false)
    private List<Form> forms;

    private Map<String, Form> formRepo = new HashMap<>();

    @Nullable
    @Override
    public String submitForm(Form form) throws Exception {
        return "Received formID = " + form.getId();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Optional.ofNullable(this.forms).ifPresent(forms -> forms.forEach(form -> this.formRepo.put(form.getId(), form)));
    }
}
