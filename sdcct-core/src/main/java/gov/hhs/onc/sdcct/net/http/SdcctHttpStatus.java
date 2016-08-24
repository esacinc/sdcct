package gov.hhs.onc.sdcct.net.http;

import javax.annotation.Nonnegative;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import org.springframework.http.HttpStatus;

public enum SdcctHttpStatus implements StatusType {
    CONTINUE, SWITCHING_PROTOCOLS, PROCESSING, CHECKPOINT, OK, CREATED, ACCEPTED, NON_AUTHORITATIVE_INFORMATION, NO_CONTENT, RESET_CONTENT, PARTIAL_CONTENT,
    MULTI_STATUS, ALREADY_REPORTED, IM_USED, MULTIPLE_CHOICES, MOVED_PERMANENTLY, FOUND, MOVED_TEMPORARILY, SEE_OTHER, NOT_MODIFIED, USE_PROXY,
    TEMPORARY_REDIRECT, PERMANENT_REDIRECT, BAD_REQUEST, UNAUTHORIZED, PAYMENT_REQUIRED, FORBIDDEN, NOT_FOUND, METHOD_NOT_ALLOWED, NOT_ACCEPTABLE,
    PROXY_AUTHENTICATION_REQUIRED, REQUEST_TIMEOUT, CONFLICT, GONE, LENGTH_REQUIRED, PRECONDITION_FAILED, PAYLOAD_TOO_LARGE, REQUEST_ENTITY_TOO_LARGE,
    URI_TOO_LONG, REQUEST_URI_TOO_LONG, UNSUPPORTED_MEDIA_TYPE, REQUESTED_RANGE_NOT_SATISFIABLE, EXPECTATION_FAILED, I_AM_A_TEAPOT,
    INSUFFICIENT_SPACE_ON_RESOURCE, METHOD_FAILURE, DESTINATION_LOCKED, UNPROCESSABLE_ENTITY, LOCKED, FAILED_DEPENDENCY, UPGRADE_REQUIRED,
    PRECONDITION_REQUIRED, TOO_MANY_REQUESTS, REQUEST_HEADER_FIELDS_TOO_LARGE, UNAVAILABLE_FOR_LEGAL_REASONS, INTERNAL_SERVER_ERROR, NOT_IMPLEMENTED,
    BAD_GATEWAY, SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT, HTTP_VERSION_NOT_SUPPORTED, VARIANT_ALSO_NEGOTIATES, INSUFFICIENT_STORAGE, LOOP_DETECTED,
    BANDWIDTH_LIMIT_EXCEEDED, NOT_EXTENDED, NETWORK_AUTHENTICATION_REQUIRED;

    private final int code;
    private final String reasonPhrase;
    private final Family family;

    private SdcctHttpStatus() {
        HttpStatus status = HttpStatus.valueOf(this.name());
        this.family = Family.familyOf((this.code = status.value()));
        this.reasonPhrase = status.getReasonPhrase();
    }

    @Nonnegative
    @Override
    public int getStatusCode() {
        return this.code;
    }

    @Override
    public Family getFamily() {
        return this.family;
    }

    @Override
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
