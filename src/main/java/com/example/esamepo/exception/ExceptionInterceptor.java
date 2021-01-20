package com.example.esamepo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * class for handling response to an exception
 */
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    /**
     * method for handling response to a UserException,
     * @param ex te UserException to handle
     * @return te correct http Status and json response
     * @see ServerException
     */
    @ExceptionHandler(UserException.class)
    public final ResponseEntity<Object> handleBadRequestException(UserException ex) {
        MyExceptionSchema exceptionResponse = new MyExceptionSchema(ex.getMessage(), ex.getDetails(), ex.getTodo());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * method for handling response to a MethodArgumentTypeMismatchException
     * @param originalException the MethodArgumentTypeMismatchException to handle
     * @return te correct http Status and json response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleInvalidParameterValue(MethodArgumentTypeMismatchException originalException) {
        String offendingValue = originalException.getValue().toString();
        UserException ex = new UserException(offendingValue + " is not a valid number", "Provide a positive integer as a count");
        MyExceptionSchema exceptionResponse = new MyExceptionSchema(ex.getMessage(), ex.getDetails(), ex.getTodo());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * method for handling response to a ServerException,
     * @param ex te ServerException to handle
     * @return te correct http Status and json response
     * @see UserException
     */
    @ExceptionHandler(ServerException.class)
    public final ResponseEntity<Object> handleConnectivityException(ServerException ex) {
        MyExceptionSchema exceptionResponse = new MyExceptionSchema(ex.getMessage(), ex.getDetails(), ex.getTodo());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
