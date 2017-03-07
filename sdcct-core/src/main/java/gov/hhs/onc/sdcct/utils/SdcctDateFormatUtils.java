package gov.hhs.onc.sdcct.utils;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public final class SdcctDateFormatUtils {
    public static enum DateTimePrecisionType implements IdentifiedBean {
        MS(HOUR_MIN_SEC_MS_PATTERN, HOUR_MIN_SEC_MS_FORMAT_PATTERN, HOUR_MIN_SEC_MS_TZ_FORMAT_PATTERN, Calendar.MILLISECOND),
        SEC(HOUR_MIN_SEC_PATTERN, HOUR_MIN_SEC_FORMAT_PATTERN, HOUR_MIN_SEC_TZ_FORMAT_PATTERN, Calendar.SECOND),
        MIN(HOUR_MIN_PATTERN, HOUR_MIN_FORMAT_PATTERN, HOUR_MIN_TZ_FORMAT_PATTERN, Calendar.MINUTE),
        DAY(YEAR_MONTH_DAY_PATTERN, YEAR_MONTH_DAY_FORMAT_PATTERN, YEAR_MONTH_DAY_TZ_FORMAT_PATTERN, Calendar.DAY_OF_MONTH),
        MONTH(YEAR_MONTH_PATTERN, YEAR_MONTH_FORMAT_PATTERN, YEAR_MONTH_TZ_FORMAT_PATTERN, Calendar.MONTH),
        YEAR(YEAR_PATTERN, YEAR_FORMAT_PATTERN, YEAR_TZ_FORMAT_PATTERN, Calendar.YEAR);

        private final String id;
        private final Pattern pattern;
        private final String formatPattern;
        private final String tzFormatPattern;
        private final int calendarField;

        private DateTimePrecisionType(Pattern pattern, String formatPattern, String tzFormatPattern, int calendarField) {
            this.id = this.name().toLowerCase();
            this.pattern = pattern;
            this.formatPattern = formatPattern;
            this.tzFormatPattern = tzFormatPattern;
            this.calendarField = calendarField;
        }

        public int getCalendarField() {
            return this.calendarField;
        }

        public String getFormatPattern() {
            return this.formatPattern;
        }

        @Override
        public String getId() {
            return this.id;
        }

        public Pattern getPattern() {
            return this.pattern;
        }

        public String getTimeZoneFormatPattern() {
            return this.tzFormatPattern;
        }
    }

    public static class ParsedDate {
        private DateTimePrecisionType precisionType;
        private Date value;
        private Date startValue;
        private Date endValue;

        public ParsedDate(DateTimePrecisionType precisionType, Date value, Date startValue, Date endValue) {
            this.precisionType = precisionType;
            this.value = value;
            this.startValue = startValue;
            this.endValue = endValue;
        }

        public Date getEndValue() {
            return this.endValue;
        }

        public DateTimePrecisionType getPrecisionType() {
            return this.precisionType;
        }

        public Date getStartValue() {
            return this.startValue;
        }

        public Date getValue() {
            return this.value;
        }
    }

    public final static String TZ_PATTERN_GROUP_NAME = "tz";

    public final static String TZ_FORMAT_PATTERN_SUFFIX = "XXX";

    public final static String TZ_PATTERN_STR =
        ("(?<" + TZ_PATTERN_GROUP_NAME + ">" + SdcctDateUtils.UTC_ZULU_TZ_ID + "|[\\-\\+](?:[0-1]\\d|2[0-3]):[0-5]\\d|)");

    public final static String YEAR_PATTERN_STR = "(\\d{4})";
    public final static Pattern YEAR_PATTERN = Pattern.compile((SdcctStringUtils.CARET + YEAR_PATTERN_STR + TZ_PATTERN_STR + SdcctStringUtils.DOLLAR_SIGN));
    public final static String YEAR_FORMAT_PATTERN = "yyyy";
    public final static String YEAR_TZ_FORMAT_PATTERN = YEAR_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String YEAR_MONTH_PATTERN_STR = (YEAR_PATTERN_STR + "\\-(0\\d|1[0-2])");
    public final static Pattern YEAR_MONTH_PATTERN =
        Pattern.compile((SdcctStringUtils.CARET + YEAR_MONTH_PATTERN_STR + TZ_PATTERN_STR + SdcctStringUtils.DOLLAR_SIGN));
    public final static String YEAR_MONTH_FORMAT_PATTERN = YEAR_FORMAT_PATTERN + "-MM";
    public final static String YEAR_MONTH_TZ_FORMAT_PATTERN = YEAR_MONTH_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String YEAR_MONTH_DAY_PATTERN_STR = (YEAR_MONTH_PATTERN_STR + "\\-([0-2]\\d|3[0-1])");
    public final static Pattern YEAR_MONTH_DAY_PATTERN =
        Pattern.compile((SdcctStringUtils.CARET + YEAR_MONTH_DAY_PATTERN_STR + TZ_PATTERN_STR + SdcctStringUtils.DOLLAR_SIGN));
    public final static String YEAR_MONTH_DAY_FORMAT_PATTERN = YEAR_MONTH_FORMAT_PATTERN + "-dd";
    public final static String YEAR_MONTH_DAY_TZ_FORMAT_PATTERN = YEAR_MONTH_DAY_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String HOUR_MIN_PATTERN_STR = (YEAR_MONTH_DAY_PATTERN_STR + "T([0-1]\\d|2[0-3]):([0-5]\\d)");
    public final static Pattern HOUR_MIN_PATTERN =
        Pattern.compile((SdcctStringUtils.CARET + HOUR_MIN_PATTERN_STR + TZ_PATTERN_STR + SdcctStringUtils.DOLLAR_SIGN));
    public final static String HOUR_MIN_FORMAT_PATTERN = YEAR_MONTH_DAY_FORMAT_PATTERN + "'T'HH:mm";
    public final static String HOUR_MIN_TZ_FORMAT_PATTERN = HOUR_MIN_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String HOUR_MIN_SEC_PATTERN_STR = (HOUR_MIN_PATTERN_STR + ":([0-5]\\d)");
    public final static Pattern HOUR_MIN_SEC_PATTERN =
        Pattern.compile((SdcctStringUtils.CARET + HOUR_MIN_SEC_PATTERN_STR + TZ_PATTERN_STR + SdcctStringUtils.DOLLAR_SIGN));
    public final static String HOUR_MIN_SEC_FORMAT_PATTERN = HOUR_MIN_FORMAT_PATTERN + ":ss";
    public final static String HOUR_MIN_SEC_TZ_FORMAT_PATTERN = HOUR_MIN_SEC_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String HOUR_MIN_SEC_MS_PATTERN_STR = (HOUR_MIN_SEC_PATTERN_STR + "\\.(\\d{3})");
    public final static Pattern HOUR_MIN_SEC_MS_PATTERN =
        Pattern.compile((SdcctStringUtils.CARET + HOUR_MIN_SEC_MS_PATTERN_STR + TZ_PATTERN_STR + SdcctStringUtils.DOLLAR_SIGN));
    public final static String HOUR_MIN_SEC_MS_FORMAT_PATTERN = HOUR_MIN_SEC_FORMAT_PATTERN + ".SSS";
    public final static String HOUR_MIN_SEC_MS_TZ_FORMAT_PATTERN = HOUR_MIN_SEC_MS_FORMAT_PATTERN + TZ_FORMAT_PATTERN_SUFFIX;

    public final static String DISPLAY_FORMAT_PATTERN = HOUR_MIN_SEC_TZ_FORMAT_PATTERN;
    public final static FastDateFormat DISPLAY_FORMAT = FastDateFormat.getInstance(DISPLAY_FORMAT_PATTERN);

    private SdcctDateFormatUtils() {
    }

    public static String format(Date date, DateTimePrecisionType precisionType, boolean withTz, @Nullable TimeZone tz, @Nullable Locale locale) {
        return FastDateFormat.getInstance((withTz ? precisionType.getTimeZoneFormatPattern() : precisionType.getFormatPattern()),
            ((tz != null) ? tz : SdcctDateUtils.DEFAULT_TZ), ((locale != null) ? locale : SdcctLocaleUtils.DEFAULT_LOCALE)).format(date);
    }

    public static String format(FastDateFormat dateFormat, long timestamp, @Nullable TimeZone timeZone) {
        return (((timeZone != null) && !dateFormat.getTimeZone().equals(timeZone)) ? FastDateFormat.getInstance(dateFormat.getPattern(), timeZone) : dateFormat)
            .format(timestamp);
    }

    public static ParsedDate parse(String str, @Nullable TimeZone tz, @Nullable Locale locale) throws ParseException {
        Matcher matcher = null;
        DateTimePrecisionType precisionType = null;
        int precisionCalendarField = -1;

        for (DateTimePrecisionType precisionTypeItem : DateTimePrecisionType.values()) {
            if ((matcher = precisionTypeItem.getPattern().matcher(str)).matches()) {
                precisionCalendarField = (precisionType = precisionTypeItem).getCalendarField();

                break;
            }
        }

        if (precisionType == null) {
            throw new ParseException(String.format("Unable to determine date/time string precision type: %s", str), 0);
        }

        if (tz == null) {
            tz = SdcctDateUtils.DEFAULT_TZ;
        }

        if (locale == null) {
            locale = SdcctLocaleUtils.DEFAULT_LOCALE;
        }

        String matcherTzGroup = matcher.group(TZ_PATTERN_GROUP_NAME);
        boolean withTz = !matcherTzGroup.isEmpty();
        TimeZone parseTz = (withTz ? TimeZone.getTimeZone(matcherTzGroup) : tz);

        Calendar calendar = Calendar.getInstance(parseTz, locale);
        calendar.setTime(
            FastDateFormat.getInstance((withTz ? precisionType.getTimeZoneFormatPattern() : precisionType.getFormatPattern()), parseTz, locale).parse(str));
        calendar.setTimeZone(tz);

        return new ParsedDate(precisionType, calendar.getTime(), DateUtils.truncate(calendar, precisionCalendarField).getTime(),
            DateUtils.ceiling(calendar, precisionCalendarField).getTime());
    }
}
