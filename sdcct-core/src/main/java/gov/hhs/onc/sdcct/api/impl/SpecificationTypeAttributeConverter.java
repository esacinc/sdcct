package gov.hhs.onc.sdcct.api.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.data.db.convert.impl.AbstractIdentifiedEnumAttributeConverter;
import javax.persistence.Converter;
import org.springframework.stereotype.Component;

@Component("attrConvSpecType")
@Converter(autoApply = true)
public class SpecificationTypeAttributeConverter extends AbstractIdentifiedEnumAttributeConverter<SpecificationType> {
    public SpecificationTypeAttributeConverter() {
        super(SpecificationType.class);
    }
}
