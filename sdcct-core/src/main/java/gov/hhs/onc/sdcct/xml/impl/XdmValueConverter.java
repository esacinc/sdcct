package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.convert.SdcctConditionalGenericConverter;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmValue;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convXdmValue")
public class XdmValueConverter implements SdcctConditionalGenericConverter {
    private final static Set<ConvertiblePair> CONV_TYPES = Stream.of(Boolean.class, Number.class, QName.class, String.class, URI.class)
        .map(srcClass -> new ConvertiblePair(srcClass, XdmValue.class)).collect(Collectors.toSet());

    @Nullable
    @Override
    public Object convert(Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
        if (!this.matches(srcType, targetType)) {
            return null;
        }

        Class<?> srcObjType = srcType.getObjectType();

        if (srcObjType.equals(Boolean.class)) {
            return new XdmAtomicValue(((Boolean) src));
        } else if (srcObjType.equals(Integer.class) || srcObjType.equals(Long.class)) {
            return new XdmAtomicValue(((Long) src));
        } else if (srcObjType.equals(Float.class)) {
            return new XdmAtomicValue(((Float) src));
        } else if (srcObjType.equals(Double.class)) {
            return new XdmAtomicValue(((Double) src));
        } else if (srcObjType.equals(QName.class)) {
            return new XdmAtomicValue(((QName) src));
        } else if (srcObjType.equals(URI.class)) {
            return new XdmAtomicValue(((URI) src));
        } else {
            return new XdmAtomicValue(((String) src));
        }
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONV_TYPES;
    }
}
