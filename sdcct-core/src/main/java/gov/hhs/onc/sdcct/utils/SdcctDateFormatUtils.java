package gov.hhs.onc.sdcct.utils;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.time.FastDateFormat;

public final class SdcctDateFormatUtils {
    public static enum DateTimePrecisionType implements IdentifiedBean {
        YEAR(YEAR_FORMAT_PATTERN, false), YEAR_TZ(YEAR_TZ_FORMAT_PATTERN, true), MONTH(YEAR_MONTH_FORMAT_PATTERN, false),
        MONTH_TZ(YEAR_MONTH_TZ_FORMAT_PATTERN, true), DAY(YEAR_MONTH_DAY_FORMAT_PATTERN, false), DAY_TZ(YEAR_MONTH_DAY_TZ_FORMAT_PATTERN, true),
        MIN(HOUR_MIN_FORMAT_PATTERN, false), MIN_TZ(HOUR_MIN_TZ_FORMAT_PATTERN, true), SEC(HOUR_MIN_SEC_FORMAT_PATTERN, false),
        SEC_TZ(HOUR_MIN_SEC_TZ_FORMAT_PATTERN, true), MS(HOUR_MIN_SEC_MS_FORMAT_PATTERN, false), MS_TZ(HOUR_MIN_SEC_MS_TZ_FORMAT_PATTERN, true);

        private final String id;
        private final String formatPattern;
        private final boolean withTz;

        private DateTimePrecisionType(String formatPattern, boolean withTz) {
            this.id = this.name().toLowerCase();
            this.formatPattern = formatPattern;
            this.withTz = withTz;
        }

        public String getFormatPattern() {
            return this.formatPattern;
        }

        @Override
        public String getId() {
            return this.id;
        }

        public boolean isWithTimeZone() {
            return this.withTz;
        }
    }

    public final static String YEAR_PATTERN_GROUP_NAME = "year";
    public final static String MONTH_PATTERN_GROUP_NAME = "month";
    public final static String DAY_PATTERN_GROUP_NAME = "day";
    public final static String HOUR_PATTERN_GROUP_NAME = "hour";
    public final static String MIN_PATTERN_GROUP_NAME = "min";
    public final static String SEC_PATTERN_GROUP_NAME = "sec";
    public final static String MS_PATTERN_GROUP_NAME = "ms";
    public final static String TZ_PATTERN_GROUP_NAME = "tz";

    public final static String TIME_FORMAT_PATTERN_DELIM = "'T'";

    public final static String TZ_FORMAT_PATTERN_SUFFIX = "XXX";

    public final static String DATE_TIME_PATTERN_STR = "(?<" + YEAR_PATTERN_GROUP_NAME + ">\\d{4})(?:\\-(?<" + MONTH_PATTERN_GROUP_NAME +
        ">0?\\d|1[0-2])(?:\\-(?<" + DAY_PATTERN_GROUP_NAME + ">[0-2]?\\d|3[0-1])(?:T(?<" + HOUR_PATTERN_GROUP_NAME + ">[0-1]\\d|2[0-3]):(?<" +
        MIN_PATTERN_GROUP_NAME + ">[0-5]\\d)(?::(?<" + SEC_PATTERN_GROUP_NAME + ">[0-5]\\d)(?::(?<" + MS_PATTERN_GROUP_NAME + ">\\d{3}))?)?)?)?)?" + "(?<" +
        TZ_PATTERN_GROUP_NAME + ">" + SdcctDateUtils.UTC_ZULU_TZ_ID + "|[\\-\\+](?:[0-1]\\d|2[0-3]):[0-5]\\d|)";
    public final static Pattern DATE_TIME_PATTERN = Pattern.compile(SdcctStringUtils.CARET + DATE_TIME_PATTERN_STR + SdcctStringUtils.DOLLAR_SIGN);

    public final static String YEAR_FORMAT_PATTERN = "yyyy";
    public final static String YEAR_TZ_FORMAT_PATTERN = YEAR_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String YEAR_MONTH_FORMAT_PATTERN = YEAR_FORMAT_PATTERN + "-MM";
    public final static String YEAR_MONTH_TZ_FORMAT_PATTERN = YEAR_MONTH_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String YEAR_MONTH_DAY_FORMAT_PATTERN = YEAR_MONTH_FORMAT_PATTERN + "-dd";
    public final static String YEAR_MONTH_DAY_TZ_FORMAT_PATTERN = YEAR_MONTH_DAY_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String HOUR_MIN_FORMAT_PATTERN = YEAR_MONTH_DAY_FORMAT_PATTERN + TIME_FORMAT_PATTERN_DELIM + "HH:mm";
    public final static String HOUR_MIN_TZ_FORMAT_PATTERN = HOUR_MIN_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String HOUR_MIN_SEC_FORMAT_PATTERN = HOUR_MIN_FORMAT_PATTERN + ":ss";
    public final static String HOUR_MIN_SEC_TZ_FORMAT_PATTERN = HOUR_MIN_SEC_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String HOUR_MIN_SEC_MS_FORMAT_PATTERN = HOUR_MIN_SEC_FORMAT_PATTERN + ":SSS";
    public final static String HOUR_MIN_SEC_MS_TZ_FORMAT_PATTERN = HOUR_MIN_SEC_MS_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    private SdcctDateFormatUtils() {
    }

    public static String format(Date date, DateTimePrecisionType precisionType, @Nullable TimeZone tz, @Nullable Locale locale) {
        return FastDateFormat.getInstance(precisionType.getFormatPattern(), ((tz != null) ? tz : SdcctDateUtils.DEFAULT_TZ),
            ((locale != null) ? locale : SdcctLocaleUtils.DEFAULT_LOCALE)).format(date);
    }

    public static Date parse(String str, @Nullable TimeZone tz, @Nullable Locale locale) throws ParseException {
        return buildFormat(str, tz, locale).parse(str);
    }

    public static FastDateFormat buildFormat(String str, @Nullable TimeZone tz, @Nullable Locale locale) {
        Matcher matcher = DATE_TIME_PATTERN.matcher(str);
        String group = matcher.group(TZ_PATTERN_GROUP_NAME);
        boolean withTz = !group.isEmpty();

        if (tz == null) {
            tz = (withTz ? TimeZone.getTimeZone(group) : SdcctDateUtils.DEFAULT_TZ);
        }

        return FastDateFormat.getInstance(findPrecisionType(matcher, withTz).getFormatPattern(), tz,
            ((locale != null) ? locale : SdcctLocaleUtils.DEFAULT_LOCALE));
    }

    public static DateTimePrecisionType findPrecisionType(String str) {
        Matcher matcher = DATE_TIME_PATTERN.matcher(str);

        return findPrecisionType(matcher, !matcher.group(TZ_PATTERN_GROUP_NAME).isEmpty());
    }

    public static boolean isValid(String str) {
        return DATE_TIME_PATTERN.matcher(str).matches();
    }

    private static DateTimePrecisionType findPrecisionType(Matcher matcher, boolean withTz) {
        if (matcher.group(MS_PATTERN_GROUP_NAME) != null) {
            return (withTz ? DateTimePrecisionType.MS_TZ : DateTimePrecisionType.MS);
        } else if (matcher.group(SEC_PATTERN_GROUP_NAME) != null) {
            return (withTz ? DateTimePrecisionType.SEC_TZ : DateTimePrecisionType.SEC);
        } else if ((matcher.group(HOUR_PATTERN_GROUP_NAME) != null) && (matcher.group(MIN_PATTERN_GROUP_NAME) != null)) {
            return (withTz ? DateTimePrecisionType.MIN_TZ : DateTimePrecisionType.MIN);
        } else if (matcher.group(DAY_PATTERN_GROUP_NAME) != null) {
            return (withTz ? DateTimePrecisionType.DAY_TZ : DateTimePrecisionType.DAY);
        } else if (matcher.group(MONTH_PATTERN_GROUP_NAME) != null) {
            return (withTz ? DateTimePrecisionType.MONTH_TZ : DateTimePrecisionType.MONTH);
        } else {
            return (withTz ? DateTimePrecisionType.YEAR_TZ : DateTimePrecisionType.YEAR);
        }
    }
}
