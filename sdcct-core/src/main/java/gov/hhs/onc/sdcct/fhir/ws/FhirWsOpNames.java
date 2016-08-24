package gov.hhs.onc.sdcct.fhir.ws;

public final class FhirWsOpNames {
    public final static String EXPAND = "expand";
    public final static String META = "meta";
    public final static String META_ADD = META + "-add";
    public final static String META_DELETE = META + "-delete";
    public final static String VALIDATE = "validate";
    public final static String VALIDATE_CODE = VALIDATE + "-code";

    private FhirWsOpNames() {
    }
}
