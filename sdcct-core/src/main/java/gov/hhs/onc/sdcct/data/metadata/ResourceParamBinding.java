package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.beans.ValueSetBean;
import gov.hhs.onc.sdcct.fhir.BindingStrength;

public interface ResourceParamBinding extends ValueSetBean {
    public BindingStrength getStrength();
}
