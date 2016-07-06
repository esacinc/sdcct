package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import javax.xml.bind.JAXBElement;

public class SdcctJaxbModule extends JaxbAnnotationModule {
    public static interface JaxbElementMixIn {
        @JsonValue
        public Object getValue();
    }

    @Override
    public void setupModule(SetupContext context) {
        this._nonNillableInclusion = Include.NON_DEFAULT;
        this._priority = Priority.SECONDARY;

        super.setupModule(context);

        context.setMixInAnnotations(JAXBElement.class, JaxbElementMixIn.class);
    }
}
