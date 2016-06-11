package gov.hhs.onc.sdcct.data.db;

public final class DbSequenceNames {
    public final static String RESOURCE_PREFIX = "resource_";

    public final static String ENTITY_ID_SUFFIX = "entity_id";

    public final static String RESOURCE_ENTITY_ID = RESOURCE_PREFIX + ENTITY_ID_SUFFIX;
    public final static String RESOURCE_PARAM_ENTITY_ID = RESOURCE_PREFIX + "param_" + ENTITY_ID_SUFFIX;

    private DbSequenceNames() {
    }
}
