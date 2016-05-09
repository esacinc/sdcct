package gov.hhs.onc.sdcct.data.db;

public final class DbSequenceNames {
    public final static String ID_SUFFIX = "id";

    public final static String RESOURCE_ID = "resource_" + ID_SUFFIX;
    public final static String REVISION_ID = "revision_" + ID_SUFFIX;
    public final static String SEARCH_PARAM_ID = "search_param_" + ID_SUFFIX;

    private DbSequenceNames() {
    }
}
