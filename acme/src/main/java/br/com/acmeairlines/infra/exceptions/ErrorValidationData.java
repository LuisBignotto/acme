package br.com.acmeairlines.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorValidationData {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity treatingError400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ErrorValidationDataRecord::new).toList());
    }
    private record ErrorValidationDataRecord(String field, String msg) {
        public ErrorValidationDataRecord(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
