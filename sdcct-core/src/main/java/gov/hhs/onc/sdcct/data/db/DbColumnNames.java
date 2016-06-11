package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public final class DbColumnNames {
    public final static String CODE_SYSTEM_PREFIX = "code_system_";
    public final static String ENTITY_PREFIX = "entity_";

    public final static String TIMESTAMP_SUFFIX = "timestamp";

    public final static String CONTENT = "content";
    public final static String DELETED_TIMESTAMP = "deleted_" + TIMESTAMP_SUFFIX;
    public final static String ID = "id";
    public final static String INSTANCE_ID = "instance_" + ID;
    public final static String META = "meta";
    public final static String MODIFIED_TIMESTAMP = "modified_" + TIMESTAMP_SUFFIX;
    public final static String NAME = "name";
    public final static String PUBLISHED_TIMESTAMP = "published_" + TIMESTAMP_SUFFIX;
    public final static String TEXT = "text";
    public final static String TYPE = "type";
    public final static String SPEC_TYPE = "spec_" + TYPE;
    public final static String UNITS = "units";
    public final static String VALUE = "value";
    public final static String VALUE_END = VALUE + SdcctStringUtils.UNDERSCORE + "end";
    public final static String VALUE_START = VALUE + "_start";
    public final static String VERSION = "version";

    public final static String CODE_SYSTEM_URI = CODE_SYSTEM_PREFIX + "uri";
    public final static String CODE_SYSTEM_VERSION = CODE_SYSTEM_PREFIX + VERSION;
    public final static String DISPLAY_NAME = "display_" + NAME;
    public final static String ENTITY_ID = ENTITY_PREFIX + ID;
    public final static String ENTITY_VERSION = ENTITY_PREFIX + VERSION;
    public final static String RESOURCE_ENTITY_ID = "resource_" + ENTITY_ID;

    private DbColumnNames() {
    }
}
