package com.aryan.featureflags.exception;


import com.aryan.featureflags.dto.ErrorResponse;
import com.aryan.featureflags.model.Feature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeatureNotFound(FeatureNotFoundException e){
        ErrorResponse error= new ErrorResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(FeatureAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFeatureAlreadyExists(
            FeatureAlreadyExistsException e) {
        ErrorResponse error= new ErrorResponse(
                e.getMessage(),
                HttpStatus.CONFLICT.value()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
