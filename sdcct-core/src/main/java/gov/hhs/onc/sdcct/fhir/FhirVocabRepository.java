package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import java.util.Map;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.InitializingBean;

public interface FhirVocabRepository extends InitializingBean {
    public MultiKeyMap<String, CodeableConcept> getValueSetConcepts();

    public Map<String, ValueSet> getValueSets();

    public ResourceSource getValueSetsSource();

    public void setValueSetsSource(ResourceSource valueSetsSrc);
}
