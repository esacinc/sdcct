package gov.hhs.onc.sdcct.data.db.convert.impl;

import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Converter;
import org.springframework.stereotype.Component;

@Component("attrConvUri")
@Converter(autoApply = true)
public class UriAttributeConverter extends AbstractStringAttributeConverter<URI> {
    public UriAttributeConverter() {
        super(URI.class);
    }

    @Nullable
    @Override
    protected URI convertToEntityAttributeInternal(@Nullable String dbValue) throws Exception {
        return ((dbValue != null) ? URI.create(dbValue) : null);
    }
}
