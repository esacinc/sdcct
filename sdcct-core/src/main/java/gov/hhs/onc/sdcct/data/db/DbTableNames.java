package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public final class DbTableNames {
    public final static String PARAM_PREFIX = "param_";

    public final static String RESOURCE = "resource";
    public final static String RESOURCE_PARAM_DATE = RESOURCE + SdcctStringUtils.UNDERSCORE + PARAM_PREFIX + "date";
    public final static String RESOURCE_PARAM_NUMBER = RESOURCE + SdcctStringUtils.UNDERSCORE + PARAM_PREFIX + "number";
    public final static String RESOURCE_PARAM_QUANTITY = RESOURCE + SdcctStringUtils.UNDERSCORE + PARAM_PREFIX + "quantity";
    public final static String RESOURCE_PARAM_REF = RESOURCE + SdcctStringUtils.UNDERSCORE + PARAM_PREFIX + "ref";
    public final static String RESOURCE_PARAM_STRING = RESOURCE + SdcctStringUtils.UNDERSCORE + PARAM_PREFIX + "string";
    public final static String RESOURCE_PARAM_TOKEN = RESOURCE + SdcctStringUtils.UNDERSCORE + PARAM_PREFIX + "token";
    public final static String RESOURCE_PARAM_URI = RESOURCE + SdcctStringUtils.UNDERSCORE + PARAM_PREFIX + "uri";

    private DbTableNames() {
    }
}
