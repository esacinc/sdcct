package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.Code;
import gov.hhs.onc.sdcct.fhir.CodeableConcept;
import gov.hhs.onc.sdcct.fhir.FhirVocabRepository;
import gov.hhs.onc.sdcct.fhir.StringValue;
import gov.hhs.onc.sdcct.fhir.Uri;
import gov.hhs.onc.sdcct.fhir.ValueSet;
import gov.hhs.onc.sdcct.fhir.ValueSetCodeSystem;
import gov.hhs.onc.sdcct.fhir.ValueSetConcept;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;

public class FhirVocabRepositoryImpl implements FhirVocabRepository {
    @Autowired
    private XmlCodec xmlCodec;

    private ResourceSource valueSetsSrc;
    private Map<String, ValueSet> valueSets;
    private MultiKeyMap<String, CodeableConcept> valueSetConcepts;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.valueSets =
            SdcctStreamUtils
                .asInstances(this.xmlCodec.decode(this.valueSetsSrc, BundleImpl.class, null).getEntry().stream()
                    .map(bundleEntry -> bundleEntry.getResource().getContent()), ValueSet.class)
            .collect(SdcctStreamUtils.toMap(valueSet -> valueSet.getId().getValue(), Function.identity(), TreeMap::new));

        this.valueSetConcepts = new MultiKeyMap<>();

        ValueSet valueSet;
        ValueSetCodeSystem valueSetCodeSystem;
        Uri valueSetCodeSystemUrl;
        StringValue valueSetCodeSystemVersion;
        Code valueSetConceptCode;

        for (String valueSetId : this.valueSets.keySet()) {
            if (!(valueSet = this.valueSets.get(valueSetId)).hasCodeSystem()) {
                continue;
            }

            valueSetCodeSystemUrl = (valueSetCodeSystem = valueSet.getCodeSystem()).getSystem();
            valueSetCodeSystemVersion = valueSetCodeSystem.getVersion();

            for (ValueSetConcept valueSetConcept : valueSetCodeSystem.getConcept()) {
                this.valueSetConcepts.put(valueSetId, (valueSetConceptCode = valueSetConcept.getCode()).getValue(),
                    new CodeableConceptImpl().setCoding(Collections.singletonList(new CodingImpl().setCode(valueSetConceptCode)
                        .setDisplay(valueSetConcept.getDisplay()).setSystem(valueSetCodeSystemUrl).setVersion(valueSetCodeSystemVersion))));
            }
        }
    }

    @Override
    public MultiKeyMap<String, CodeableConcept> getValueSetConcepts() {
        return this.valueSetConcepts;
    }

    @Override
    public Map<String, ValueSet> getValueSets() {
        return this.valueSets;
    }

    @Override
    public ResourceSource getValueSetsSource() {
        return this.valueSetsSrc;
    }

    @Override
    public void setValueSetsSource(ResourceSource valueSetsSrc) {
        this.valueSetsSrc = valueSetsSrc;
    }
}
