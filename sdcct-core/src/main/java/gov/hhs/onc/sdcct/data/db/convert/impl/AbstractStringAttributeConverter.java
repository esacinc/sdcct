package gov.hhs.onc.sdcct.data.db.convert.impl;

import java.util.Objects;
import javax.annotation.Nullable;

public abstract class AbstractStringAttributeConverter<T> extends AbstractSdcctAttributeConverter<T, String> {
    protected AbstractStringAttributeConverter(Class<T> entityValueClass) {
        super(entityValueClass, String.class);
    }

    @Nullable
    @Override
    protected String convertToDatabaseColumnInternal(@Nullable T entityValue) throws Exception {
        return Objects.toString(entityValue, null);
    }
}
