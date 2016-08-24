package gov.hhs.onc.sdcct.fhir.json.impl;

import com.fasterxml.jackson.databind.BeanDescription;
import gov.hhs.onc.sdcct.fhir.Element;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.json.impl.AbstractSdcctBeanDeserializerModifier;
import org.springframework.stereotype.Component;

@Component("beanDeserializerModFhir")
public class FhirBeanDeserializerModifier extends AbstractSdcctBeanDeserializerModifier {
    @Override
    public boolean canModify(BeanDescription desc) {
        Class<?> clazz = desc.getBeanClass();

        return (Element.class.isAssignableFrom(clazz) || Resource.class.isAssignableFrom(clazz));
    }
}
