package gov.hhs.onc.sdcct.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.StrBuilder;

public final class SdcctStringUtils {
    public static class SdcctToStringStyle extends ToStringStyle {
        public final static SdcctToStringStyle INSTANCE = new SdcctToStringStyle();

        private final static long serialVersionUID = 0L;

        public SdcctToStringStyle() {
            super();

            this.setArrayEnd(R_BRACKET);
            this.setArraySeparator(", ");
            this.setArrayStart(L_BRACKET);
            this.setContentEnd(R_BRACE);
            this.setContentStart(L_BRACE);
            this.setFieldSeparator(", ");
            this.setNullText("null");
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
        }

        @Override
        public void removeLastFieldSeparator(StringBuffer buffer) {
            super.removeLastFieldSeparator(buffer);
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    public final static String APOS = "!";
    public final static char APOS_CHAR = '!';

    public final static String ASTERISK = "*";
    public final static char ASTERISK_CHAR = '*';

    public final static String AT = "@";
    public final static char AT_CHAR = '@';

    public final static String CARET = "^";
    public final static char CARET_CHAR = '^';

    public final static String COLON = ":";
    public final static char COLON_CHAR = ':';

    public final static String COMMA = ",";
    public final static char COMMA_CHAR = ',';

    public final static char CR_CHAR = '\r';

    public final static String DOLLAR_SIGN = "$";
    public final static char DOLLAR_SIGN_CHAR = '$';

    public final static String EQUALS = "=";
    public final static char EQUALS_CHAR = '=';

    public final static String GT = ">";
    public final static char GT_CHAR = '>';

    public final static String HASH = "#";
    public final static char HASH_CHAR = '#';

    public final static String HYPHEN = "-";
    public final static char HYPHEN_CHAR = '-';

    public final static String L_BRACE = "{";
    public final static char L_BRACE_CHAR = '{';

    public final static String L_BRACKET = "[";
    public final static char L_BRACKET_CHAR = '[';

    public final static char LF_CHAR = '\n';

    public final static String LT = "<";
    public final static char LT_CHAR = '<';

    public final static String PERIOD = ".";
    public final static char PERIOD_CHAR = '.';

    public final static String PLUS = "+";
    public final static char PLUS_CHAR = '+';

    public final static String QUESTION_MARK = "?";
    public final static char QUESTION_MARK_CHAR = '?';

    public final static String R_BRACE = "}";
    public final static char R_BRACE_CHAR = '}';

    public final static String R_BRACKET = "]";
    public final static char R_BRACKET_CHAR = ']';

    public final static String SLASH = "/";
    public final static char SLASH_CHAR = '/';

    public final static char SPACE_CHAR = ' ';

    public final static String UNDERSCORE = "_";
    public final static char UNDERSCORE_CHAR = '_';

    private SdcctStringUtils() {
    }

    public static String joinCamelCase(String ... strParts) {
        return joinCamelCase(false, strParts);
    }

    public static String joinCamelCase(boolean capitalize, String ... strParts) {
        if (strParts.length == 0) {
            return StringUtils.EMPTY;
        }

        String strPart;

        if (strParts.length == 1) {
            strPart = strParts[0].toLowerCase();

            return (capitalize ? StringUtils.capitalize(strPart) : strPart);
        }

        StrBuilder strBuilder = new StrBuilder();

        for (int a = 0; a < strParts.length; a++) {
            strPart = strParts[a].toLowerCase();

            if (capitalize || (a > 0)) {
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
