package gov.hhs.onc.sdcct.fhir.ws;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public final class FhirWsPaths {
    public final static String METADATA = SdcctStringUtils.SLASH + "metadata";
    public final static String OP =
        SdcctStringUtils.SLASH + SdcctStringUtils.DOLLAR_SIGN + SdcctStringUtils.L_BRACE + FhirWsPathParamNames.OP + SdcctStringUtils.R_BRACE;
    public final static String HISTORY =
        SdcctStringUtils.SLASH + "_history" + SdcctStringUtils.SLASH + SdcctStringUtils.L_BRACE + FhirWsPathParamNames.VERSION + SdcctStringUtils.R_BRACE;
    public final static String SEARCH = SdcctStringUtils.SLASH + "_search";

    public final static String TYPE = SdcctStringUtils.SLASH + SdcctStringUtils.L_BRACE + FhirWsPathParamNames.TYPE + SdcctStringUtils.R_BRACE;
    public final static String HISTORY_TYPE = TYPE + HISTORY;
    public final static String OP_TYPE = TYPE + OP;
    public final static String SEARCH_TYPE = TYPE + SEARCH;

    public final static String INSTANCE = TYPE + SdcctStringUtils.SLASH + SdcctStringUtils.L_BRACE + FhirWsPathParamNames.ID + SdcctStringUtils.R_BRACE;
    public final static String OP_INSTANCE = INSTANCE + OP;

    public final static String HISTORY_INSTANCE = INSTANCE + HISTORY;
    public final static String HISTORY_INSTANCE_OP = HISTORY_INSTANCE + OP;

    private FhirWsPaths() {
    }
}
