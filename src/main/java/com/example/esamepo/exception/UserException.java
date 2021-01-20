package com.example.esamepo.exception;

/**
 * class for handling user exception,
 * launched when the user does not provide correct input
 */
public class UserException extends RuntimeException{

    /**
     * needed for the serialization of RuntimeException
     */
    private static final long serialVersionUID = 1L;

    /**
     * main exception message, always set to "UserException"
     */
    private static final String message= "UserException";

    /**
     * details of the exception
     */
    private String details;

    /**
     * action to make for resolving the error
     */
    private String todo;

    /**
     * constructor
     * @param details details of the exception
     * @param todo action to make for resolving the error
     */

    public UserException( String details, String todo) {
        this.details = details;
        this.todo = todo;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
