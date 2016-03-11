package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.data.ResourceType;
import gov.hhs.onc.sdcct.data.db.convert.impl.AbstractIdentifiedEnumAttributeConverter;
import javax.persistence.Converter;
import org.springframework.stereotype.Component;

@Component("attrConvResourceType")
@Converter(autoApply = true)
public class ResourceTypeAttributeConverter extends AbstractIdentifiedEnumAttributeConverter<ResourceType> {
    public ResourceTypeAttributeConverter() {
        super(ResourceType.class);
    }
}
