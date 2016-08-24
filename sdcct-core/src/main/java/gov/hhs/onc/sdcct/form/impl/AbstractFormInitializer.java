package gov.hhs.onc.sdcct.form.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.initialize.impl.AbstractSdcctResourceInitializer;
import gov.hhs.onc.sdcct.form.FormInitializer;
import gov.hhs.onc.sdcct.form.SdcctForm;
import java.util.List;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFormInitializer<T, U extends SdcctResource, V extends SdcctResourceRegistry<T, ?, U>, W extends SdcctForm<T>>
    extends AbstractSdcctResourceInitializer<T, U, V> implements FormInitializer<T, U, V, W> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected List<W> forms;

    protected AbstractFormInitializer(V registry) {
        super(registry);
    }

    @Override
    protected void initializeInternal() {
        for (W form : this.forms) {
            try {
                if (!form.isInternal() || this.registry.exists(this.buildCriteria(form))) {
                    continue;
                }

                this.registry.saveBean(form.getBean());
            } catch (Exception e) {
                throw new FatalBeanException(String.format("Unable to initialize %s form (name=%s, identifier=%s) resource (beanClass=%s, beanImplClass=%s).",
                    form.getSpecificationType().getId(), form.getName(), form.getIdentifier(), form.getBeanClass(), form.getBeanImplClass()), e);
            }
        }
    }

    protected abstract SdcctCriteria<U> buildCriteria(W form);
}
