package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

@Analyzer(impl = StandardAnalyzer.class)
@Cache(region = DbTableNames.FORM_RFD, usage = CacheConcurrencyStrategy.NONE)
@Cacheable
@Entity(name = "formRfd")
@Indexed(index = DbTableNames.FORM_RFD)
@Table(name = DbTableNames.FORM_RFD)
public class RfdFormImpl extends AbstractRfdResource implements RfdForm {
}
