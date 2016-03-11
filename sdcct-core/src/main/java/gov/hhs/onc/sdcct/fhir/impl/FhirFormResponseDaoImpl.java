package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDao;
import gov.hhs.onc.sdcct.fhir.FhirFormResponse;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseDao;
import org.springframework.stereotype.Repository;

@Repository("daoFormRespFhir")
public class FhirFormResponseDaoImpl extends AbstractSdcctDao<FhirFormResponse> implements FhirFormResponseDao {
    public FhirFormResponseDaoImpl() {
        super(FhirFormResponse.class, FhirFormResponseImpl.class);
    }
}
