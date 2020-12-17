package com.example.esamepo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//this is the controlleradvice for handling custom springboot exceptions
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        MyExceptionSchema exceptionResponse = new MyExceptionSchema(ex.getMessage(), ex.getDetails(), ex.getTodo());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiSchemaException.class)
    public final ResponseEntity<Object> handleApiSchemaException(ApiSchemaException ex) {
        MyExceptionSchema exceptionResponse = new MyExceptionSchema(ex.getMessage(), ex.getDetails(), ex.getTodo());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.I_AM_A_TEAPOT);
    }
}
