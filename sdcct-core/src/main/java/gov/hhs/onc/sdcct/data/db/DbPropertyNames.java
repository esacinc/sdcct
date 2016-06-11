package gov.hhs.onc.sdcct.data.db;

public final class DbPropertyNames {
    public final static String CODE_SYSTEM_PREFIX = "codeSystem";
    public final static String ENTITY_PREFIX = "entity";

    public final static String ID_SUFFIX = "Id";
    public final static String PARAMS_SUFFIX = "Params";
    public final static String TIMESTAMP_SUFFIX = "Timestamp";
    public final static String VALUE_SUFFIX = "Value";
    public final static String VERSION_SUFFIX = "Version";

    public final static String CODE_SYSTEM_URI = CODE_SYSTEM_PREFIX + "Uri";
    public final static String CODE_SYSTEM_VERSION = CODE_SYSTEM_PREFIX + VERSION_SUFFIX;
    public final static String CONTENT = "content";
    public final static String DATE_PARAMS = "date" + PARAMS_SUFFIX;
    public final static String DELETED_TIMESTAMP = "deleted" + TIMESTAMP_SUFFIX;
    public final static String END_VALUE = "end" + VALUE_SUFFIX;
    public final static String ENTITY_ID = ENTITY_PREFIX + ID_SUFFIX;
    public final static String ENTITY_VERSION = ENTITY_PREFIX + VERSION_SUFFIX;
    public final static String ID = "id";
    public final static String INSTANCE_ID = "instance" + ID_SUFFIX;
    public final static String MODIFIED_TIMESTAMP = "modified" + TIMESTAMP_SUFFIX;
    public final static String NAME = "name";
    public final static String NUMBER_PARAMS = "number" + PARAMS_SUFFIX;
    public final static String PUBLISHED_TIMESTAMP = "published" + TIMESTAMP_SUFFIX;
    public final static String REF_PARAMS = "ref" + PARAMS_SUFFIX;
    public final static String RESOURCE_ENTITY_ID = "resourceEntity" + ID_SUFFIX;
    public final static String START_VALUE = "start" + VALUE_SUFFIX;
    public final static String STRING_PARAMS = "string" + PARAMS_SUFFIX;
    public final static String TEXT = "text";
    public final static String TOKEN_PARAMS = "token" + PARAMS_SUFFIX;
    public final static String TYPE = "type";
    public final static String UNITS = "units";
    public final static String URI_PARAMS = "uri" + PARAMS_SUFFIX;
    public final static String VALUE = "value";
    public final static String VERSION = "version";

    private DbPropertyNames() {
    }
}
