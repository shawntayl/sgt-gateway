package com.sgt.student.service.gateway.handler;

import com.sgt.student.service.gateway.response.error.GatewayApiError;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.ws.soap.client.SoapFaultClientException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class provides centralized exception handling across
 * all {@code @RequestMapping} methods through {@code @ExceptionHandler} methods.
 * The{@code @ControllerAdvice} annotation enabling it to be shared across multiple @Controller classes.
 */

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = Logger.getLogger(RestResponseEntityExceptionHandler.class.getName());

    private static final String SHOULD_BE_OF_TYPE = " should be of type ";
    private static final String COLON_SPACE = ": ";
    private static final String PARAMETER_IS_MISSING = " parameter is missing";


    /**
     * To handle Connection and SocketTimeout exceptions, caused by underlying web service calls
     * @param exception the intercepted exception that needs handling
     * @return a {@code ResponseEntity} GatewayApiError instance
     */
    @ExceptionHandler({ConnectException.class, SocketTimeoutException.class, SoapFaultClientException.class, NullPointerException.class})
    public ResponseEntity<GatewayApiError> internalServerExceptions(final Exception exception) {
        final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        final GatewayApiError gatewayApiError = new GatewayApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, exception.getLocalizedMessage());
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<GatewayApiError>(gatewayApiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * To handle Connection and SocketTimeout exceptions, caused by underlying web service calls
     * @param exception the intercepted exception that needs handling
     * @return a {@code ResponseEntity} GatewayApiError instance
     */
    @ExceptionHandler({StudentNotFoundException.class})
    public ResponseEntity<GatewayApiError> studentNotFoundException(final StudentNotFoundException exception) {
        final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        final GatewayApiError gatewayApiError = new GatewayApiError(HttpStatus.NOT_FOUND, message, exception.getLocalizedMessage());
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<GatewayApiError>(gatewayApiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * To handle Constraint Violation exceptions
     * @param exception the intercepted exception that needs handling
     * @return a {@code ResponseEntity} GatewayApiError instance
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<GatewayApiError> handleConstraintViolation(ConstraintViolationException exception) {
        final List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final GatewayApiError gatewayApiError = new GatewayApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<GatewayApiError>(gatewayApiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * To handle Method Argument Type Mismatch exceptions
     * @param exception the intercepted exception that needs handling
     * @return a {@code ResponseEntity} GatewayApiError instance
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<GatewayApiError> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception, WebRequest request) {
        final StringBuilder stringBuilder = new StringBuilder(exception.getName());
        stringBuilder.append(SHOULD_BE_OF_TYPE)
                .append(exception.getRequiredType().getName());
        final String error = stringBuilder.toString();

        final GatewayApiError gatewayApiError = new GatewayApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), error);
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<GatewayApiError>(gatewayApiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Overriding handleMethodArgumentNotValid to customize the response message being returned
     * @param exception the MethodArgumentNotValidException being handled
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} GatewayApiError instance
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        final List<String> errors = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + COLON_SPACE + error.getDefaultMessage());
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + COLON_SPACE + error.getDefaultMessage());
        }

        final GatewayApiError gatewayApiError = new GatewayApiError(status, exception.getLocalizedMessage(), errors);
        LOG.error(exception.getMessage(), exception);
        return handleExceptionInternal(exception, gatewayApiError, headers, HttpStatus.valueOf(gatewayApiError.getStatus()), request);
    }

    /**
     * Overriding handleMissingServletRequestParameter to customize the response message being returned
     * @param exception the MissingServletRequestParameterException being handled
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} GatewayApiError instance
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String error = exception.getParameterName() + PARAMETER_IS_MISSING;
        final GatewayApiError gatewayApiError = new GatewayApiError(status, exception.getLocalizedMessage(), error);
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<Object>(gatewayApiError, new HttpHeaders(), HttpStatus.valueOf(gatewayApiError.getStatus()));
    }

    /**
     * Overriding handleHttpMessageNotReadable to customize the response message being returned
     * @param exception the HttpMessageNotReadableException being handled
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} GatewayApiError instance
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final Throwable mostSpecificCause = exception.getMostSpecificCause();
        GatewayApiError gatewayApiError = null;
        if (mostSpecificCause != null) {
            final String exceptionName = mostSpecificCause.getClass().getName();
            final String message = mostSpecificCause.getMessage();
            gatewayApiError = new GatewayApiError(status, exceptionName, message);
        } else {
            final String error = null;
            gatewayApiError = new GatewayApiError(status, exception.getMessage(), error);
        }
        LOG.error(exception.getMessage(), exception);
        return handleExceptionInternal(exception, gatewayApiError, headers, HttpStatus.valueOf(gatewayApiError.getStatus()), request);
    }
}

