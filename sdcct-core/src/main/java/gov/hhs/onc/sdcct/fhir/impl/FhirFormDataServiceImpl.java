package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDataService;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.FhirFormDao;
import gov.hhs.onc.sdcct.fhir.FhirFormDataService;
import org.springframework.stereotype.Service;

@Service("dataServiceFormFhir")
public class FhirFormDataServiceImpl extends AbstractSdcctDataService<FhirForm, FhirFormDao> implements FhirFormDataService {
    public FhirFormDataServiceImpl() {
        super(FhirForm.class, FhirFormImpl.class);
    }
}
