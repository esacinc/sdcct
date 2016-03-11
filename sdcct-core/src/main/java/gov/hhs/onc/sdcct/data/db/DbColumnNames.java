package gov.hhs.onc.sdcct.data.db;

public final class DbColumnNames {
    public final static String RESOURCE_PREFIX = "resource_";

    public final static String CODE_SYSTEM_URI = "code_system_uri";
    public final static String CONTENT = "content";
    public final static String ID = "id";
    public final static String ENTITY_ID = "entity_" + ID;
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";
    public final static String NAME = "name";
    public final static String RESOURCE_ENTITY_ID = RESOURCE_PREFIX + ENTITY_ID;
    public final static String TEXT = "text";
    public final static String TYPE = "type";
    public final static String UNITS = "units";
    public final static String VALUE = "value";

    private DbColumnNames() {
    }
}
