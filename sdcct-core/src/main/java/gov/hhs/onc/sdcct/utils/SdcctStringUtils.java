package gov.hhs.onc.sdcct.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

public final class SdcctStringUtils {
    public final static String COLON = ":";
    public final static char COLON_CHAR = ':';

    public final static String HYPHEN = "-";
    public final static char HYPHEN_CHAR = '-';

    public final static String PERIOD = ".";
    public final static char PERIOD_CHAR = '.';

    public final static String UNDERSCORE = "_";
    public final static char UNDERSCORE_CHAR = '_';

    private SdcctStringUtils() {
    }

    public static String joinCamelCase(String ... strParts) {
        if (strParts.length == 0) {
            return StringUtils.EMPTY;
        } else if (strParts.length == 1) {
            return strParts[0].toLowerCase();
        }

        StrBuilder strBuilder = new StrBuilder();
        String strPart;

        for (int a = 0; a < strParts.length; a++) {
            strPart = strParts[a].toLowerCase();

            if (a > 0) {
                strPart = StringUtils.capitalize(strPart);
            }

            strBuilder.append(strPart);
        }

        return strBuilder.build();
    }

    public static String[] splitCamelCase(String str, String delims) {
        if (str.isEmpty()) {
            return new String[0];
        }

        String[] initialParts = StringUtils.split(str, delims);
        List<String> parts = new ArrayList<>(initialParts.length);
        int partStartIndex, lastPartCharType, partCharType;
        char[] initialPartChars;

        for (String initialPart : initialParts) {
            if (initialPart.isEmpty()) {
                parts.add(initialPart);

                continue;
            }

            partStartIndex = -1;
            lastPartCharType = Character.getType((initialPartChars = initialPart.toCharArray())[0]);

            for (int b = 1; b < initialPartChars.length; b++) {
                partCharType = Character.getType(initialPartChars[b]);

                if (((lastPartCharType == Character.LOWERCASE_LETTER) && (partCharType == Character.UPPERCASE_LETTER))
                    || ((partStartIndex != -1) && (lastPartCharType == Character.UPPERCASE_LETTER) && (partCharType == Character.LOWERCASE_LETTER))) {
                    parts.add(new String(initialPartChars, (partStartIndex = Math.max(partStartIndex, 0)), (b - partStartIndex)));

                    partStartIndex = b;
                }

                lastPartCharType = partCharType;
            }

            if (partStartIndex == -1) {
                parts.add(initialPart);
            } else if (partStartIndex < initialPartChars.length) {
                parts.add(new String(initialPartChars, partStartIndex, (initialPartChars.length - partStartIndex)));
            }
        }

        return parts.toArray(new String[parts.size()]);
    }
}
