package gov.hhs.onc.sdcct.fhir.json.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import gov.hhs.onc.sdcct.fhir.Element;
import gov.hhs.onc.sdcct.json.impl.AbstractSdcctBeanDeserializerModifier;
import org.springframework.stereotype.Component;

@Component("beanDeserializerModifierFhir")
public class FhirBeanDeserializerModifier extends AbstractSdcctBeanDeserializerModifier {
    @Override
    public boolean canModify(BeanDescription desc) {
        return Element.class.isAssignableFrom(desc.getBeanClass());
    }
}
