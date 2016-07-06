package gov.hhs.onc.sdcct.fhir.json.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import gov.hhs.onc.sdcct.fhir.Element;
import gov.hhs.onc.sdcct.json.impl.AbstractSdcctBeanSerializerModifier;
import org.springframework.stereotype.Component;

@Component("beanSerializerModifierFhir")
public class FhirBeanSerializerModifier extends AbstractSdcctBeanSerializerModifier {
    @Override
    public boolean canModify(BeanDescription desc) {
        return Element.class.isAssignableFrom(desc.getBeanClass());
    }
}
