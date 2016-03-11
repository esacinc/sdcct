package gov.hhs.onc.sdcct.data.db.convert.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import javax.annotation.Nullable;

public abstract class AbstractIdentifiedEnumAttributeConverter<T extends Enum<T> & IdentifiedBean> extends AbstractStringAttributeConverter<T> {
    protected AbstractIdentifiedEnumAttributeConverter(Class<T> entityValueClass) {
        super(entityValueClass);
    }

    @Nullable
    @Override
    protected T convertToEntityAttributeInternal(@Nullable String dbValue) throws Exception {
        return ((dbValue != null) ? SdcctEnumUtils.findById(this.entityValueClass, dbValue) : null);
    }

    @Nullable
    @Override
    protected String convertToDatabaseColumnInternal(@Nullable T entityValue) throws Exception {
        return ((entityValue != null) ? entityValue.getId() : null);
    }
}
