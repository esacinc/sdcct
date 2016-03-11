package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDataService;
import gov.hhs.onc.sdcct.fhir.FhirFormResponse;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseDao;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseDataService;
import org.springframework.stereotype.Service;

@Service("dataServiceFormRespFhir")
public class FhirFormResponseDataServiceImpl extends AbstractSdcctDataService<FhirFormResponse, FhirFormResponseDao> implements FhirFormResponseDataService {
    public FhirFormResponseDataServiceImpl() {
        super(FhirFormResponse.class, FhirFormResponseImpl.class);
    }
}
