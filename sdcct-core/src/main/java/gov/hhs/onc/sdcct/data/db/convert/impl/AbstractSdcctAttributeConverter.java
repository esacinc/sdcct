package gov.hhs.onc.sdcct.data.db.convert.impl;

import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import org.hibernate.HibernateException;

public abstract class AbstractSdcctAttributeConverter<T, U> implements AttributeConverter<T, U> {
    protected Class<T> entityValueClass;
    protected Class<U> dbValueClass;

    protected AbstractSdcctAttributeConverter(Class<T> entityValueClass, Class<U> dbValueClass) {
        this.entityValueClass = entityValueClass;
        this.dbValueClass = dbValueClass;
    }

    @Nullable
    @Override
    public T convertToEntityAttribute(@Nullable U dbValue) {
        try {
            return this.convertToEntityAttributeInternal(dbValue);
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to convert attribute from database value (class=%s) to entity value (class=%s): %s",
                this.dbValueClass.getName(), this.entityValueClass.getName(), dbValue), e);
        }
    }

    @Nullable
    @Override
    public U convertToDatabaseColumn(@Nullable T entityValue) {
        try {
            return this.convertToDatabaseColumnInternal(entityValue);
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to convert attribute from entity value (class=%s) to database value (class=%s): %s",
                this.entityValueClass.getName(), this.dbValueClass.getName(), entityValue), e);
        }
    }

    @Nullable
    protected abstract T convertToEntityAttributeInternal(@Nullable U dbValue) throws Exception;

    @Nullable
    protected abstract U convertToDatabaseColumnInternal(@Nullable T entityValue) throws Exception;
}
