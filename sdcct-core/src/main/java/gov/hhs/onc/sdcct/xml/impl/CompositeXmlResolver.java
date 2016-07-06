package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.transform.ResourceSourceResolver;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.utils.SdcctResourceUtils;
import gov.hhs.onc.sdcct.xml.SdcctXmlResolver;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CompositeXmlResolver extends AbstractSdcctXmlResolver {
    @Order(Ordered.LOWEST_PRECEDENCE)
    public static class DefaultXmlResolver extends AbstractSdcctXmlResolver {
        private final static String RELATIVE_SCHEMA_SYS_ID_PREFIX = ("../../schema/");
        private final static String SCHEMA_SYS_ID_PREFIX =
            (ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + SdcctResourceUtils.META_INF_APP_PATH_PREFIX + "schema/**/");

        private final static String XML_SCHEMA_SYS_ID_SUFFIX = ("xml" + FilenameUtils.EXTENSION_SEPARATOR_STR + SdcctFileNameExtensions.XSD);

        private final static String XML_SCHEMA_SYS_ID = (ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/schemas/wsdl/" + XML_SCHEMA_SYS_ID_SUFFIX);

        public DefaultXmlResolver(ResourceSourceResolver resourceSrcResolver) {
            super(resourceSrcResolver);
        }

        @Nullable
        @Override
        public ByteArraySource resolve(@Nullable String nsUri, @Nullable String name, @Nullable String baseUri, @Nullable String publicId,
            @Nullable String sysId) {
            return ((Objects.equals(nsUri, XMLConstants.XML_NS_URI) || Objects.equals(publicId, XMLConstants.XML_NS_URI))
                ? super.resolve(XMLConstants.XML_NS_URI, name, null, XMLConstants.XML_NS_URI, XML_SCHEMA_SYS_ID)
                : super.resolve(nsUri, name, baseUri, publicId, (SCHEMA_SYS_ID_PREFIX + StringUtils.removeStart(sysId, RELATIVE_SCHEMA_SYS_ID_PREFIX))));
        }
    }

    private Set<SdcctXmlResolver> resolvers = Stream.of(new DefaultXmlResolver(this.resourceSrcResolver))
        .collect(Collectors.toCollection(() -> new TreeSet<>(AnnotationAwareOrderComparator.INSTANCE)));

    public CompositeXmlResolver(ResourceSourceResolver resourceSrcResolver) {
        this(resourceSrcResolver, ArrayUtils.toArray());
    }

    public CompositeXmlResolver(ResourceSourceResolver resourceSrcResolver, SdcctXmlResolver ... resolvers) {
        super(resourceSrcResolver);

        this.addResolvers(resolvers);
    }

    @Nullable
    @Override
    public Source resolve(String href, @Nullable String baseUri) throws XPathException {
        if (!this.hasResolvers()) {
            return super.resolve(href, baseUri);
        }

        Source src;

        for (SdcctXmlResolver resolver : this.resolvers) {
            if ((src = resolver.resolve(href, baseUri)) != null) {
                return src;
            }
        }

        return super.resolve(href, baseUri);
    }

    @Nullable
    @Override
    public LSInput resolveResource(String name, String nsUri, @Nullable String publicId, @Nullable String sysId, @Nullable String baseUri) {
        if (!this.hasResolvers()) {
            return super.resolveResource(name, nsUri, publicId, sysId, baseUri);
        }

        LSInput lsIn;

        for (SdcctXmlResolver resolver : this.resolvers) {
            if ((lsIn = resolver.resolveResource(name, nsUri, publicId, sysId, baseUri)) != null) {
                return lsIn;
            }
        }

        return super.resolveResource(name, nsUri, publicId, sysId, baseUri);
    }

    @Nullable
    @Override
    public InputSource resolveEntity(@Nullable String publicId, String sysId) throws IOException, SAXException {
        if (!this.hasResolvers()) {
            return super.resolveEntity(publicId, sysId);
        }

        InputSource inSrc;

        for (SdcctXmlResolver resolver : this.resolvers) {
            if ((inSrc = resolver.resolveEntity(publicId, sysId)) != null) {
                return inSrc;
            }
        }

        return super.resolveEntity(publicId, sysId);
    }

    @Nullable
    @Override
    public InputSource resolveEntity(String name, @Nullable String publicId, @Nullable String baseUri, String sysId) {
        if (!this.hasResolvers()) {
            return super.resolveEntity(name, publicId, baseUri, sysId);
        }

        InputSource inSrc;

        for (SdcctXmlResolver resolver : this.resolvers) {
            if ((inSrc = resolver.resolveEntity(name, publicId, baseUri, sysId)) != null) {
                return inSrc;
            }
        }

        return super.resolveEntity(name, publicId, baseUri, sysId);
    }

    @Nullable
    @Override
    public ByteArraySource resolve(@Nullable String nsUri, @Nullable String name, @Nullable String baseUri, @Nullable String publicId, @Nullable String sysId) {
        if (!this.hasResolvers()) {
            return super.resolve(nsUri, name, baseUri, publicId, sysId);
        }

        ByteArraySource src;

        for (SdcctXmlResolver resolver : this.resolvers) {
            if ((src = resolver.resolve(nsUri, name, baseUri, publicId, sysId)) != null) {
                return src;
            }
        }

        return super.resolve(nsUri, name, baseUri, publicId, sysId);
    }

    @Nullable
    @Override
    public InputSource getExternalSubset(String name, @Nullable String baseUri) throws IOException, SAXException {
        if (!this.hasResolvers()) {
            return super.getExternalSubset(name, baseUri);
        }

        InputSource inputSrc;

        for (SdcctXmlResolver resolver : this.resolvers) {
            if ((inputSrc = resolver.getExternalSubset(name, baseUri)) != null) {
                return inputSrc;
            }
        }

        return super.getExternalSubset(name, baseUri);
    }

    public void addResolvers(SdcctXmlResolver ... resolvers) {
        Stream.of(resolvers).forEach(this.resolvers::add);
    }

    public boolean hasResolvers() {
        return !this.resolvers.isEmpty();
    }

    public Set<SdcctXmlResolver> getResolvers() {
        return this.resolvers;
    }
}
