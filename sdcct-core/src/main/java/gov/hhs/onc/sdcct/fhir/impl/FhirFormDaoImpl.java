package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctDao;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.FhirFormDao;
import org.springframework.stereotype.Repository;

@Repository("daoFormFhir")
public class FhirFormDaoImpl extends AbstractSdcctDao<FhirForm> implements FhirFormDao {
    public FhirFormDaoImpl() {
        super(FhirForm.class, FhirFormImpl.class);
    }
}
