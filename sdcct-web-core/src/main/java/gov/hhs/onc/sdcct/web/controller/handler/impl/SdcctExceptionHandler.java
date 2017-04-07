package gov.hhs.onc.sdcct.web.controller.handler.impl;

import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseProcessingException;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.web.controller.handler.ErrorJsonWrapper;
import gov.hhs.onc.sdcct.web.controller.handler.ErrorsJsonWrapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class SdcctExceptionHandler extends ResponseEntityExceptionHandler {
    private final static Map<String, String> DEFAULT_RESP_HEADERS =
        Stream.of(new ImmutablePair<>(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)).collect(SdcctStreamUtils.toMap(LinkedHashMap::new));

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleExceptionException(WebRequest req, Exception exception) {
        return this.handleExceptionInternal(exception, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    @ExceptionHandler({ SdcctTestcaseProcessingException.class })
    public ResponseEntity<Object> handleTestcaseProcessingException(WebRequest req, SdcctTestcaseProcessingException exception) {
        return this.handleExceptionInternal(exception, buildBindingResultErrors(exception, exception.getBindingResult()), new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders respHeaders, HttpStatus respStatus,
        WebRequest req) {
        return this.handleExceptionInternal(exception, buildBindingResultErrors(exception, exception.getBindingResult()), respHeaders, respStatus, req);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders respHeaders, HttpStatus respStatus, WebRequest req) {
        return this.handleExceptionInternal(exception, buildBindingResultErrors(exception, exception.getBindingResult()), respHeaders, respStatus, req);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object respBody, HttpHeaders respHeaders, HttpStatus respStatus,
        WebRequest req) {
        if (respBody == null) {
            respBody = new ErrorsJsonWrapperImpl().addGlobalErrors(new ErrorJsonWrapperImpl(exception));

            respHeaders.setAll(DEFAULT_RESP_HEADERS);
        }

        return super.handleExceptionInternal(exception, respBody, respHeaders, respStatus, req);
    }

    private static ErrorsJsonWrapper buildBindingResultErrors(Exception exception, @Nullable BindingResult bindingResult) {
        ErrorsJsonWrapper errors = new ErrorsJsonWrapperImpl();

        if (bindingResult == null) {
            return errors.addGlobalErrors(new ErrorJsonWrapperImpl(exception));
        }

        if (bindingResult.hasGlobalErrors()) {
            errors.setGlobalErrors(
                bindingResult.getGlobalErrors().stream().map(globalError -> new ErrorJsonWrapperImpl(globalError.toString())).collect(Collectors.toList()));
        }

        if (bindingResult.hasFieldErrors()) {
            ListMultimap<String, ErrorJsonWrapper> fieldErrors = errors.getFieldErrors();

            bindingResult.getFieldErrors().forEach(fieldError -> fieldErrors.put(fieldError.getField(), new ErrorJsonWrapperImpl(fieldError.toString())));
        }

        return errors;
    }
}
