package gov.hhs.onc.sdcct.data.db;

public final class DbPropertyNames {
    public final static String CODE_SYSTEM_PREFIX = "codeSystem";

    public final static String SEARCH_PARAMS_SUFFIX = "SearchParams";
    public final static String VALUE_SUFFIX = "Value";
    public final static String VERSION_SUFFIX = "Version";

    public final static String CODE_SYSTEM_URI = CODE_SYSTEM_PREFIX + "Uri";
    public final static String CODE_SYSTEM_VERSION = CODE_SYSTEM_PREFIX + VERSION_SUFFIX;
    public final static String COMPOSITE_SEARCH_PARAMS = "composite" + SEARCH_PARAMS_SUFFIX;
    public final static String CONTENT = "content";
    public final static String DATE_SEARCH_PARAMS = "date" + SEARCH_PARAMS_SUFFIX;
    public final static String END_VALUE = "end" + VALUE_SUFFIX;
    public final static String ID = "id";
    public final static String IDENTIFIER = "identifier";
    public final static String META = "meta";
    public final static String NAME = "name";
    public final static String NUMBER_SEARCH_PARAMS = "number" + SEARCH_PARAMS_SUFFIX;
    public final static String RESOURCE = "resource";
    public final static String RESOURCE_ID = RESOURCE + "Id";
    public final static String RESOURCE_VERSION = RESOURCE + VERSION_SUFFIX;
    public final static String QUANTITY_SEARCH_PARAMS = "quantity" + SEARCH_PARAMS_SUFFIX;
    public final static String REF_SEARCH_PARAMS = "ref" + SEARCH_PARAMS_SUFFIX;
    public final static String STANDARD_TYPE = "standardType";
    public final static String START_VALUE = "start" + VALUE_SUFFIX;
    public final static String STRING_SEARCH_PARAMS = "string" + SEARCH_PARAMS_SUFFIX;
    public final static String TEXT = "text";
    public final static String TOKEN_SEARCH_PARAMS = "token" + SEARCH_PARAMS_SUFFIX;
    public final static String TYPE = "type";
    public final static String UNITS = "units";
    public final static String URI_SEARCH_PARAMS = "uri" + SEARCH_PARAMS_SUFFIX;
    public final static String VALUE = "value";
    public final static String VERSION = "version";

    private DbPropertyNames() {
    }
}
