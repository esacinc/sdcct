package gov.hhs.onc.sdcct.xml.utils;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import net.sf.saxon.s9api.QName;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public final class SdcctXmlQnameUtils {
    private SdcctXmlQnameUtils() {
    }

    public static String buildQualifiedName(QName qname) {
        String prefix = qname.getPrefix(), localName = qname.getLocalName();

        return (StringUtils.isEmpty(prefix) ? localName : (prefix + SdcctStringUtils.COLON + localName));
    }

    public static QName build(@Nullable String nsPrefix, @Nullable String nsUri, String localName) {
        return new QName(ObjectUtils.defaultIfNull(nsPrefix, XMLConstants.DEFAULT_NS_PREFIX), ObjectUtils.defaultIfNull(nsUri, XMLConstants.NULL_NS_URI),
            localName);
    }
}
