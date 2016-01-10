package gov.hhs.onc.sdcct.io.impl;

import gov.hhs.onc.sdcct.io.ResourceSourceResolver;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.transform.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

@Component("convResourceSrc")
public class ResourceSourceConverter implements ConditionalGenericConverter {
    private final static Set<ConvertiblePair> CONV_TYPES =
        Stream.of(new ConvertiblePair(String.class, Source.class), new ConvertiblePair(String.class, ResourceSource.class)).collect(Collectors.toSet());

    @Autowired
    private ResourceSourceResolver resourceSrcResolver;

    @Nullable
    @Override
    public Object convert(Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
        if (!this.matches(srcType, targetType)) {
            return null;
        }

        try {
            return this.resourceSrcResolver.resolve(((String) src));
        } catch (IOException e) {
            throw new ConversionFailedException(TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(ResourceSource.class), src, e);
        }
    }

    @Override
    public boolean matches(TypeDescriptor srcType, TypeDescriptor targetType) {
        return CONV_TYPES.stream().anyMatch(convType -> (srcType.isAssignableTo(TypeDescriptor.valueOf(convType.getSourceType()))
            && targetType.isAssignableTo(TypeDescriptor.valueOf(convType.getTargetType()))));
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONV_TYPES;
    }
}
