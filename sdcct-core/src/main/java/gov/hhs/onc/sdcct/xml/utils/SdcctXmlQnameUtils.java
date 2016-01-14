package gov.hhs.onc.sdcct.xml.utils;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.StringUtils;

public final class SdcctXmlQnameUtils {
    private SdcctXmlQnameUtils() {
    }

    public static String buildQualifiedName(QName qname) {
        String prefix = qname.getPrefix(), localPart = qname.getLocalPart();

        return (StringUtils.isEmpty(prefix) ? localPart : (prefix + SdcctStringUtils.COLON + localPart));
    }
}
