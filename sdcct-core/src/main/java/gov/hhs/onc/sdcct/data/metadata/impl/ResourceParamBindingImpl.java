package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.data.metadata.ResourceParamBinding;
import gov.hhs.onc.sdcct.fhir.BindingStrength;
import java.net.URI;

public class ResourceParamBindingImpl implements ResourceParamBinding {
    private URI valueSetUri;
    private BindingStrength strength;

    public ResourceParamBindingImpl(URI valueSetUri, BindingStrength strength) {
        this.valueSetUri = valueSetUri;
        this.strength = strength;
    }

    @Override
    public BindingStrength getStrength() {
        return this.strength;
    }

    @Override
    public URI getValueSetUri() {
        return this.valueSetUri;
    }
}
