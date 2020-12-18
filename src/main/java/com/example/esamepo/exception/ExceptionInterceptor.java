package com.example.esamepo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//this is the controlleradvice for handling custom springboot exceptions
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserException.class)
    public final ResponseEntity<Object> handleBadRequestException(UserException ex) {
        MyExceptionSchema exceptionResponse = new MyExceptionSchema(ex.getMessage(), ex.getDetails(), ex.getTodo());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerException.class)
    public final ResponseEntity<Object> handleConnectivityException(ServerException ex) {
        MyExceptionSchema exceptionResponse = new MyExceptionSchema(ex.getMessage(), ex.getDetails(), ex.getTodo());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
