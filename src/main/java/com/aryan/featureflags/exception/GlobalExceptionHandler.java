package com.aryan.featureflags.exception;


import com.aryan.featureflags.model.Feature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<String> handleFeatureNotFound( FeatureNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    }

    @ExceptionHandler(FeatureAlreadyExistsException.class)
    public ResponseEntity<String> handleFeatureAlreadyExists(
            FeatureAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
