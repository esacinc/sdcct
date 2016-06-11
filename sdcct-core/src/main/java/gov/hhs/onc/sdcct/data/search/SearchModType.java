package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.fhir.SearchModifierCode;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum SearchModType implements IdentifiedBean {
    MISSING(null, SearchModifierCode.MISSING, ResourceParamType.values()), EXACT(null, SearchModifierCode.EXACT, ResourceParamType.STRING), CONTAINS(null,
        SearchModifierCode.CONTAINS, ResourceParamType.STRING), TEXT(null, SearchModifierCode.TEXT, ResourceParamType.TOKEN), IN(null, SearchModifierCode.IN,
        ResourceParamType.TOKEN), NOT_IN("not-in", SearchModifierCode.NOT_IN, ResourceParamType.TOKEN), TYPE(null, SearchModifierCode.TYPE,
        ResourceParamType.REFERENCE), BELOW(null, SearchModifierCode.BELOW, ResourceParamType.TOKEN, ResourceParamType.URI), ABOVE(null, SearchModifierCode.ABOVE,
        ResourceParamType.TOKEN, ResourceParamType.URI);

    private final String id;
    private final SearchModifierCode fhirType;
    private final Set<ResourceParamType> resourceParamTypes;

    private SearchModType(@Nullable String id, SearchModifierCode fhirType, ResourceParamType... resourceParamTypes) {
        this.id = ((id != null) ? id : this.name().toLowerCase());
        this.fhirType = fhirType;
        this.resourceParamTypes = Stream.of(resourceParamTypes).collect(Collectors.toSet());
    }

    public SearchModifierCode getFhirType() {
        return this.fhirType;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Set<ResourceParamType> getResourceParamTypes() {
        return this.resourceParamTypes;
    }
}
