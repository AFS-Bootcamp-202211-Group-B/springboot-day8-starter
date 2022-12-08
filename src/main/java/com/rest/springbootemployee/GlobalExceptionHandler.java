package com.rest.springbootemployee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NoEmployeeFoundException.class, NoCompanyFoundException.class})

    public ResponseEntity<Object> handleExceptions() {
        return new ResponseEntity<>("resource not found",HttpStatus.NOT_FOUND);
    }
}
