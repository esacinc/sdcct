package gov.hhs.onc.sdcct.data.db;

public final class DbTableNames {
    public final static String SEARCH_PARAM_PREFIX = "search_param_";

    public final static String RESOURCE = "resource";
    public final static String RESOURCE_HISTORY = RESOURCE + "_history";
    public final static String REVISION = "revision";
    public final static String SEARCH_PARAM_DATE = SEARCH_PARAM_PREFIX + "date";
    public final static String SEARCH_PARAM_NUMBER = SEARCH_PARAM_PREFIX + "number";
    public final static String SEARCH_PARAM_QUANTITY = SEARCH_PARAM_PREFIX + "quantity";
    public final static String SEARCH_PARAM_REF = SEARCH_PARAM_PREFIX + "ref";
    public final static String SEARCH_PARAM_STRING = SEARCH_PARAM_PREFIX + "string";
    public final static String SEARCH_PARAM_TOKEN = SEARCH_PARAM_PREFIX + "token";
    public final static String SEARCH_PARAM_URI = SEARCH_PARAM_PREFIX + "uri";

    private DbTableNames() {
    }
}
