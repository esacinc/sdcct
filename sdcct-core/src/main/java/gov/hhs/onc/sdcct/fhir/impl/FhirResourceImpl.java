package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.beans.SpecificationNames;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Indexed;

@Audited
@Cache(region = DbTableNames.RESOURCE, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Cacheable
@DiscriminatorValue(SpecificationNames.FHIR)
@Entity(name = "resourceFhir")
@Indexed(index = DbTableNames.RESOURCE)
public class FhirResourceImpl extends SdcctResourceImpl implements FhirResource {
    private final static long serialVersionUID = 0L;

    public FhirResourceImpl() {
        this.specType = SpecificationType.FHIR;
    }
}
