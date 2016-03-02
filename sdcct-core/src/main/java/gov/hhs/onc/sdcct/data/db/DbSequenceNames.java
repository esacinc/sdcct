package gov.hhs.onc.sdcct.data.db;

public final class DbSequenceNames {
    public final static String RESOURCE_PREFIX = "resource_";
    public final static String SEARCH_PARAM_PREFIX = "search_param_";

    public final static String ENTITY_ID_SUFFIX = "entity_id";

    public final static String RESOURCE_ENTITY_ID = RESOURCE_PREFIX + ENTITY_ID_SUFFIX;
    public final static String SEARCH_PARAM_ENTITY_ID = SEARCH_PARAM_PREFIX + ENTITY_ID_SUFFIX;

    private DbSequenceNames() {
    }
}
