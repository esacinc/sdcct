package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public final class DbColumnNames {
    public final static String CODE_SYSTEM_PREFIX = "code_system_";
    public final static String RESOURCE_PREFIX = "resource_";
    public final static String REVISION_PREFIX = "revision_";

    public final static String END_SUFFIX = "end";

    public final static String CONTENT = "content";
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String TEXT = "text";
    public final static String TYPE = "type";
    public final static String SPEC_TYPE = "spec_" + TYPE;
    public final static String TIMESTAMP = "timestamp";
    public final static String UNITS = "units";
    public final static String VALUE = "value";
    public final static String VALUE_END = VALUE + SdcctStringUtils.UNDERSCORE + END_SUFFIX;
    public final static String VALUE_START = VALUE + "_start";
    public final static String VERSION = "version";

    public final static String CODE_SYSTEM_URI = CODE_SYSTEM_PREFIX + "uri";
    public final static String CODE_SYSTEM_VERSION = CODE_SYSTEM_PREFIX + VERSION;

    public final static String DISPLAY_NAME = "display_" + NAME;

    public final static String RESOURCE_ID = RESOURCE_PREFIX + ID;
    public final static String RESOURCE_VERSION = RESOURCE_PREFIX + VERSION;

    public final static String REVISION_END_ID = REVISION_PREFIX + END_SUFFIX + SdcctStringUtils.UNDERSCORE + ID;
    public final static String REVISION_END_TIMESTAMP = REVISION_PREFIX + END_SUFFIX + SdcctStringUtils.UNDERSCORE + TIMESTAMP;
    public final static String REVISION_ID = REVISION_PREFIX + ID;
    public final static String REVISION_TYPE = REVISION_PREFIX + TYPE;

    private DbColumnNames() {
    }
}
