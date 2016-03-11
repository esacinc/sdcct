package gov.hhs.onc.sdcct.build.xml.jaxb.impl

import gov.hhs.onc.sdcct.build.xml.jaxb.CodegenNameConverter
import java.util.regex.Pattern
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.StrBuilder

class CodegenNameConverterImpl implements CodegenNameConverter {
    protected final static Pattern UPPERCASE_SEQ_PATTERN = Pattern.compile("[A-Z]{2,}")

    @Override
    String toPackageName(String nsUri) {
        return smart.toPackageName(nsUri)
    }

    @Override
    String toClassName(String token) {
        return processName(smart.toClassName(token))
    }
    
    @Override
    String toInterfaceName(String token) {
        return processName(smart.toInterfaceName(token))
    }

    @Override
    String toConstantName(String token) {
        return smart.toConstantName(token)
    }

    @Override
    String toPropertyName(String token) {
        return processName(smart.toPropertyName(token))
    }

    @Override
    String toVariableName(String token) {
        return smart.toVariableName(token)
    }

    protected static String processName(String name) {
        def ucSeqMatcher = UPPERCASE_SEQ_PATTERN.matcher(name)

        if (!ucSeqMatcher.matches()) {
            return name
        }

        def nameLen = name.length(), lastUcSeqEndIndex = -1, ucSeqStartIndex
        def nameBuilder = new StrBuilder(nameLen)

        while ((++lastUcSeqEndIndex < nameLen) && ucSeqMatcher.find(lastUcSeqEndIndex)) {
            if ((ucSeqStartIndex = ucSeqMatcher.start()) > lastUcSeqEndIndex) {
                nameBuilder.append(name.substring(lastUcSeqEndIndex, ucSeqStartIndex))
            }

            nameBuilder.append(StringUtils.capitalize(name.substring(ucSeqStartIndex, (lastUcSeqEndIndex = ucSeqMatcher.end())).toLowerCase()))
        }

        if (!ucSeqMatcher.hitEnd()) {
            nameBuilder.append(name.substring(lastUcSeqEndIndex))
        }

        return nameBuilder.build()
    }
}
