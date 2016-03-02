package gov.hhs.onc.sdcct.data.db.impl;

import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ClassUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.StringType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public abstract class AbstractVarcharType<T> extends AbstractSingleColumnStandardBasicType<T> implements DiscriminatorType<T> {
    protected abstract static class AbstractVarcharTypeDescriptor<T> extends AbstractTypeDescriptor<T> {
        protected Function<String, T> fromStrFunc;
        protected Function<T, String> toStrFunc;

        private final static long serialVersionUID = 0L;

        protected AbstractVarcharTypeDescriptor(Class<T> typeClass, Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
            super(typeClass);

            this.fromStrFunc = fromStrFunc;
            this.toStrFunc = toStrFunc;
        }

        @Override
        public String toString(T value) {
            return this.toStrFunc.apply(value);
        }

        @Override
        public T fromString(String str) {
            return this.fromStrFunc.apply(str);
        }

        @Nullable
        @Override
        public <X> X unwrap(@Nullable T value, Class<X> unwrappedTypeClass, WrapperOptions wrapperOpts) {
            if (value == null) {
                return null;
            } else if (ClassUtils.isAssignable(unwrappedTypeClass, String.class)) {
                return unwrappedTypeClass.cast(this.toString(value));
            } else {
                throw unknownUnwrap(unwrappedTypeClass);
            }
        }

        @Nullable
        @Override
        public <X> T wrap(@Nullable X unwrappedValue, WrapperOptions wrapperOpts) {
            if (unwrappedValue == null) {
                return null;
            } else if (unwrappedValue instanceof String) {
                return this.fromString(((String) unwrappedValue));
            } else {
                throw unknownWrap(unwrappedValue.getClass());
            }
        }
    }

    protected Class<T> typeClass;

    private final static long serialVersionUID = 0L;

    protected AbstractVarcharType(Class<T> typeClass, JavaTypeDescriptor<T> javaTypeDesc) {
        super(VarcharTypeDescriptor.INSTANCE, javaTypeDesc);

        this.typeClass = typeClass;
    }

    @Override
    public T stringToObject(String str) throws Exception {
        return this.fromString(str);
    }

    @Override
    public String objectToSQLString(T value, Dialect dialect) throws Exception {
        return StringType.INSTANCE.objectToSQLString(this.toString(value), dialect);
    }

    @Override
    public String getName() {
        return this.typeClass.getSimpleName().toLowerCase();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
