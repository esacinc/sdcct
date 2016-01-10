package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.form.FormWebService;

public interface FhirFormWebService<T extends FormService> extends FormWebService<T> {
}
