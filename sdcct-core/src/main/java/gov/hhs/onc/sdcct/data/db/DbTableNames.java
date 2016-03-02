package gov.hhs.onc.sdcct.data.db;

public final class DbTableNames {
    public final static String FORM_PREFIX = "form_";
    public final static String SEARCH_PARAM_PREFIX = "search_param_";

    public final static String FHIR_SUFFIX = "fhir";
    public final static String RFD_SUFFIX = "rfd";

    public final static String FORM_FHIR = FORM_PREFIX + FHIR_SUFFIX;
    public final static String FORM_RFD = FORM_PREFIX + RFD_SUFFIX;
    public final static String RESOURCE = "resource";
    public final static String SEARCH_PARAM_COORD = SEARCH_PARAM_PREFIX + "coord";
    public final static String SEARCH_PARAM_DATE = SEARCH_PARAM_PREFIX + "date";
    public final static String SEARCH_PARAM_NUM = SEARCH_PARAM_PREFIX + "num";
    public final static String SEARCH_PARAM_QUANTITY = SEARCH_PARAM_PREFIX + "quantity";
    public final static String SEARCH_PARAM_REF = SEARCH_PARAM_PREFIX + "ref";
    public final static String SEARCH_PARAM_STR = SEARCH_PARAM_PREFIX + "str";
    public final static String SEARCH_PARAM_TOKEN = SEARCH_PARAM_PREFIX + "token";
    public final static String SEARCH_PARAM_URI = SEARCH_PARAM_PREFIX + "uri";

    private DbTableNames() {
    }
}
