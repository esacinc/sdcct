package gov.hhs.onc.sdcct.xml.saxon.convert.impl;

import gov.hhs.onc.sdcct.convert.SdcctConditionalGenericConverter;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentUri;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.sf.saxon.om.DocumentURI;
import org.springframework.core.convert.TypeDescriptor;

public class SdcctDocumentUriConverter implements SdcctConditionalGenericConverter {
    private final static Set<ConvertiblePair> CONV_TYPES =
        Stream.of(new ConvertiblePair(String.class, DocumentURI.class), new ConvertiblePair(String.class, SdcctDocumentUri.class)).collect(Collectors.toSet());

    @Nullable
    @Override
    public Object convert(Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
        if (!this.matches(srcType, targetType)) {
            return null;
        }

        return new SdcctDocumentUri(((String) src));
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONV_TYPES;
    }
}
