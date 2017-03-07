package gov.hhs.onc.sdcct.web.gateway.controller.handler.impl;

import gov.hhs.onc.sdcct.web.gateway.controller.InvalidRequestException;
import gov.hhs.onc.sdcct.web.gateway.json.ErrorsJsonWrapper;
import gov.hhs.onc.sdcct.web.gateway.json.impl.ErrorJsonWrapperImpl;
import gov.hhs.onc.sdcct.web.gateway.json.impl.ErrorsJsonWrapperImpl;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class SdcctExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ InvalidRequestException.class })
    public ResponseEntity<Object> handleInvalidRequestException(Exception exception, WebRequest request) {
        InvalidRequestException invalidReqException = (InvalidRequestException) exception;
        ErrorsJsonWrapper errorsJsonWrapper = new ErrorsJsonWrapperImpl();

        invalidReqException.getErrors().getFieldErrors().forEach(
            fieldErrorObj -> errorsJsonWrapper.getFieldErrors().put(fieldErrorObj.getField(), new ErrorJsonWrapperImpl(fieldErrorObj.getDefaultMessage())));

        errorsJsonWrapper.setGlobalErrors(invalidReqException.getErrors().getGlobalErrors().stream()
            .map(globalError -> new ErrorJsonWrapperImpl(globalError.getDefaultMessage())).collect(Collectors.toList()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return super.handleExceptionInternal(invalidReqException, errorsJsonWrapper, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleExceptionException(Exception exception, WebRequest request) {
        ErrorsJsonWrapper errorsJsonWrapper = new ErrorsJsonWrapperImpl();
        errorsJsonWrapper.setGlobalErrors(Collections.singletonList(new ErrorJsonWrapperImpl(exception)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return super.handleExceptionInternal(exception, errorsJsonWrapper, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
