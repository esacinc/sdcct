package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.convert.impl.AbstractIdentifiedEnumAttributeConverter;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.persistence.Converter;
import org.springframework.stereotype.Component;

@Component("attrConvSearchParamType")
@Converter(autoApply = true)
public class SearchParamTypeAttributeConverter extends AbstractIdentifiedEnumAttributeConverter<SearchParamType> {
    public SearchParamTypeAttributeConverter() {
        super(SearchParamType.class);
    }
}
