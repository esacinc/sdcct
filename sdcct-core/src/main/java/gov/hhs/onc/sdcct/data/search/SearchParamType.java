package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum SearchParamType implements IdentifiedBean {
    COMPOSITE(SearchParamTypeNames.COMPOSITE, gov.hhs.onc.sdcct.fhir.SearchParamType.COMPOSITE), DATE(SearchParamTypeNames.DATE,
        gov.hhs.onc.sdcct.fhir.SearchParamType.DATE), NUMBER(SearchParamTypeNames.NUMBER, gov.hhs.onc.sdcct.fhir.SearchParamType.NUMBER), QUANTITY(
        SearchParamTypeNames.QUANTITY, gov.hhs.onc.sdcct.fhir.SearchParamType.QUANTITY), REFERENCE(SearchParamTypeNames.REFERENCE,
        gov.hhs.onc.sdcct.fhir.SearchParamType.REFERENCE), STRING(SearchParamTypeNames.STRING, gov.hhs.onc.sdcct.fhir.SearchParamType.STRING), TOKEN(
        SearchParamTypeNames.TOKEN, gov.hhs.onc.sdcct.fhir.SearchParamType.TOKEN), URI(SearchParamTypeNames.URI, gov.hhs.onc.sdcct.fhir.SearchParamType.URI);

    private final String id;
    private final gov.hhs.onc.sdcct.fhir.SearchParamType fhirType;

    private SearchParamType(String id, gov.hhs.onc.sdcct.fhir.SearchParamType fhirType) {
        this.id = id;
        this.fhirType = fhirType;
    }

    public gov.hhs.onc.sdcct.fhir.SearchParamType getFhirDefType() {
        return this.fhirType;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
