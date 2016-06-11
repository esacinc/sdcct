package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.db.convert.impl.AbstractIdentifiedEnumAttributeConverter;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import javax.persistence.Converter;
import org.springframework.stereotype.Component;

@Component("attrConvResourceParamType")
@Converter(autoApply = true)
public class ResourceParamTypeAttributeConverter extends AbstractIdentifiedEnumAttributeConverter<ResourceParamType> {
    public ResourceParamTypeAttributeConverter() {
        super(ResourceParamType.class);
    }
}
