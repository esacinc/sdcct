package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.fhir.SearchModifierCode;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum SearchModType implements IdentifiedBean {
    MISSING(null, SearchModifierCode.MISSING, SearchParamType.values()), EXACT(null, SearchModifierCode.EXACT, SearchParamType.STRING), CONTAINS(null,
        SearchModifierCode.CONTAINS, SearchParamType.STRING), TEXT(null, SearchModifierCode.TEXT, SearchParamType.TOKEN), IN(null, SearchModifierCode.IN,
        SearchParamType.TOKEN), NOT_IN("not-in", SearchModifierCode.NOT_IN, SearchParamType.TOKEN), TYPE(null, SearchModifierCode.TYPE,
        SearchParamType.REFERENCE), BELOW(null, SearchModifierCode.BELOW, SearchParamType.TOKEN, SearchParamType.URI), ABOVE(null, SearchModifierCode.ABOVE,
        SearchParamType.TOKEN, SearchParamType.URI);

    private final String id;
    private final SearchModifierCode fhirType;
    private final Set<SearchParamType> searchParamTypes;

    private SearchModType(@Nullable String id, SearchModifierCode fhirType, SearchParamType ... searchParamTypes) {
        this.id = ((id != null) ? id : this.name().toLowerCase());
        this.fhirType = fhirType;
        this.searchParamTypes = Stream.of(searchParamTypes).collect(Collectors.toSet());
    }

    public SearchModifierCode getFhirType() {
        return this.fhirType;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Set<SearchParamType> getSearchParamTypes() {
        return this.searchParamTypes;
    }
}
