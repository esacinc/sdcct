package gov.hhs.onc.sdcct.xml.qname.utils;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public final class SdcctQnameUtils {
    private SdcctQnameUtils() {
    }

    public static String buildQualifiedName(QName qname) {
        String prefix = qname.getPrefix(), localPart = qname.getLocalPart();

        return (StringUtils.isEmpty(prefix) ? localPart : (prefix + SdcctStringUtils.COLON + localPart));
    }

    public static QName build(@Nullable String nsPrefix, @Nullable String nsUri, String localPart) {
        return new QName(ObjectUtils.defaultIfNull(nsUri, XMLConstants.NULL_NS_URI), localPart,
            ObjectUtils.defaultIfNull(nsPrefix, XMLConstants.DEFAULT_NS_PREFIX));
    }
}
