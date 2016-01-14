package gov.hhs.onc.sdcct.rfd;

public final class RfdWsXmlNames {
    public final static String DELIM = "_";

    public final static String ARCHIVE_AFFIX = "Archive";
    public final static String CLARIFICATION_AFFIX = "Clarification";
    public final static String FORM_AFFIX = "Form";
    public final static String REQ_AFFIX = "Request";
    public final static String RESP_AFFIX = "Response";
    public final static String RETRIEVE_AFFIX = "Retrieve";
    public final static String SUBMIT_AFFIX = "Submit";

    public final static String RFD_PREFIX = "RFD";
    public final static String RFD_FORM_PREFIX = RFD_PREFIX + FORM_AFFIX;

    public final static String SOAP_12_SUFFIX = DELIM + "Soap12";
    public final static String BINDING_SOAP_12_SUFFIX = DELIM + "Binding" + SOAP_12_SUFFIX;
    public final static String MSG_SUFFIX = DELIM + "Message";
    public final static String SERVICE_SUFFIX = DELIM + "Service";
    public final static String PORT_SUFFIX = DELIM + "Port";
    public final static String PORT_SOAP_12_SUFFIX = PORT_SUFFIX + SOAP_12_SUFFIX;
    public final static String PORT_TYPE_SUFFIX = PORT_SUFFIX + "Type";

    public final static String FORM_ARCHIVER = RFD_FORM_PREFIX + "Archiver";
    public final static String FORM_MANAGER = RFD_FORM_PREFIX + "Manager";
    public final static String FORM_RECEIVER = RFD_FORM_PREFIX + "Receiver";

    public final static String ARCHIVE_FORM = ARCHIVE_AFFIX + FORM_AFFIX;
    public final static String ARCHIVE_FORM_REQ = ARCHIVE_FORM + REQ_AFFIX;
    public final static String ARCHIVE_FORM_REQ_MSG = ARCHIVE_FORM + MSG_SUFFIX;
    public final static String ARCHIVE_FORM_RESP = ARCHIVE_FORM + RESP_AFFIX;
    public final static String ARCHIVE_FORM_RESP_MSG = ARCHIVE_FORM_RESP + MSG_SUFFIX;
    public final static String RETRIEVE_CLARIFICATION = RETRIEVE_AFFIX + CLARIFICATION_AFFIX;
    public final static String RETRIEVE_CLARIFICATION_REQ = RETRIEVE_CLARIFICATION + REQ_AFFIX;
    public final static String RETRIEVE_CLARIFICATION_REQ_MSG = RETRIEVE_CLARIFICATION + MSG_SUFFIX;
    public final static String RETRIEVE_CLARIFICATION_RESP = RETRIEVE_CLARIFICATION + RESP_AFFIX;
    public final static String RETRIEVE_CLARIFICATION_RESP_MSG = RETRIEVE_CLARIFICATION_RESP + MSG_SUFFIX;
    public final static String RETRIEVE_FORM = RETRIEVE_AFFIX + FORM_AFFIX;
    public final static String RETRIEVE_FORM_REQ = RETRIEVE_FORM + REQ_AFFIX;
    public final static String RETRIEVE_FORM_REQ_MSG = RETRIEVE_FORM + MSG_SUFFIX;
    public final static String RETRIEVE_FORM_RESP = RETRIEVE_FORM + RESP_AFFIX;
    public final static String RETRIEVE_FORM_RESP_MSG = RETRIEVE_FORM_RESP + MSG_SUFFIX;
    public final static String SUBMIT_FORM = SUBMIT_AFFIX + FORM_AFFIX;
    public final static String SUBMIT_FORM_REQ = SUBMIT_FORM + REQ_AFFIX;
    public final static String SUBMIT_FORM_REQ_MSG = SUBMIT_FORM + MSG_SUFFIX;
    public final static String SUBMIT_FORM_RESP = SUBMIT_FORM + RESP_AFFIX;
    public final static String SUBMIT_FORM_RESP_MSG = SUBMIT_FORM_RESP + MSG_SUFFIX;

    public final static String ARCHIVE_FORM_OP = FORM_ARCHIVER + DELIM + ARCHIVE_FORM;
    public final static String RETRIEVE_CLARIFICATION_OP = FORM_MANAGER + DELIM + RETRIEVE_CLARIFICATION;
    public final static String RETRIEVE_FORM_OP = FORM_MANAGER + DELIM + RETRIEVE_FORM;
    public final static String SUBMIT_FORM_OP = FORM_RECEIVER + DELIM + SUBMIT_FORM;

    public final static String FORM_ARCHIVER_PORT_TYPE = FORM_ARCHIVER + PORT_TYPE_SUFFIX;
    public final static String FORM_ARCHIVER_BINDING = FORM_ARCHIVER + BINDING_SOAP_12_SUFFIX;
    public final static String FORM_ARCHIVER_PORT = FORM_ARCHIVER + PORT_SOAP_12_SUFFIX;
    public final static String FORM_ARCHIVER_SERVICE = FORM_ARCHIVER + SERVICE_SUFFIX;
    public final static String FORM_MANAGER_PORT_TYPE = FORM_MANAGER + PORT_TYPE_SUFFIX;
    public final static String FORM_MANAGER_BINDING = FORM_MANAGER + BINDING_SOAP_12_SUFFIX;
    public final static String FORM_MANAGER_PORT = FORM_MANAGER + PORT_SOAP_12_SUFFIX;
    public final static String FORM_MANAGER_SERVICE = FORM_MANAGER + SERVICE_SUFFIX;
    public final static String FORM_RECEIVER_PORT_TYPE = FORM_RECEIVER + PORT_TYPE_SUFFIX;
    public final static String FORM_RECEIVER_BINDING = FORM_RECEIVER + BINDING_SOAP_12_SUFFIX;
    public final static String FORM_RECEIVER_PORT = FORM_RECEIVER + PORT_SOAP_12_SUFFIX;
    public final static String FORM_RECEIVER_SERVICE = FORM_RECEIVER + SERVICE_SUFFIX;

    private RfdWsXmlNames() {
    }
}
