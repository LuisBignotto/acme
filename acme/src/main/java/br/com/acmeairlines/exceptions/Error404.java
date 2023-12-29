package br.com.acmeairlines.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class Error404 {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity treatingError404() {
        return ResponseEntity.notFound().build();
    }
}
