package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

@Analyzer(impl = StandardAnalyzer.class)
@Cache(region = DbTableNames.FORM_FHIR, usage = CacheConcurrencyStrategy.NONE)
@Cacheable
@Entity(name = "formFhir")
@Indexed(index = DbTableNames.FORM_FHIR)
@Table(name = DbTableNames.FORM_FHIR)
public class FhirFormImpl extends AbstractFhirResource implements FhirForm {
}
