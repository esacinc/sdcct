package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum SearchModType implements IdentifiedBean {
    MISSING(null, SearchParamType.values()), EXACT(null, SearchParamType.STRING), CONTAINS(null, SearchParamType.STRING), TEXT(null, SearchParamType.TOKEN),
    IN(null, SearchParamType.TOKEN), NOT_IN("not-in", SearchParamType.TOKEN), BELOW(null, SearchParamType.TOKEN, SearchParamType.URI), ABOVE(null,
        SearchParamType.TOKEN, SearchParamType.URI);

    private final String id;
    private final Set<SearchParamType> searchParamTypes;

    private SearchModType(@Nullable String id, SearchParamType ... searchParamTypes) {
        this.id = ((id != null) ? id : this.name().toLowerCase());
        this.searchParamTypes = Stream.of(searchParamTypes).collect(Collectors.toSet());
    }

    @Override
    public String getId() {
        return this.id;
    }

    public Set<SearchParamType> getSearchParamTypes() {
        return this.searchParamTypes;
    }
}
