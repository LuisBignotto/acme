package br.com.acmeairlines.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingController.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        logError(ex);
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Resource not found", request);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex, HttpServletRequest request) {
        logError(ex);
        String userMessage = messageSource.getMessage("request.invalid", null, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, userMessage, request);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(Exception ex, HttpServletRequest request) {
        logError(ex);
        String userMessage = messageSource.getMessage("authentication.failure", null, LocaleContextHolder.getLocale());
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, userMessage, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        logError(ex);
        return buildResponseEntity(HttpStatus.FORBIDDEN, "Access denied", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex, HttpServletRequest request) {
        logError(ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logError(ex);
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String userMessage = String.join(", ", errors);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, userMessage, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        logError(ex);
        return buildResponseEntity(HttpStatus.CONFLICT, "Data integrity violation", request);
    }

    @ExceptionHandler({TransactionTimedOutException.class})
    public ResponseEntity<ErrorResponse> handleTransactionTimeout(TransactionTimedOutException ex, HttpServletRequest request) {
        logError(ex);
        return buildResponseEntity(HttpStatus.REQUEST_TIMEOUT, "Transaction timed out", request);
    }

    @ExceptionHandler({ServiceUnavailableException.class})
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex, HttpServletRequest request) {
        logError(ex);
        return buildResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable", request);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaTypeAndMethod(Exception ex, HttpServletRequest request) {
        logError(ex);
        return buildResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type or method", request);
    }

    private void logError(Exception ex) {
        logger.error("Exception: {}", ex.getMessage(), ex);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
