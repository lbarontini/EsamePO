package com.example.esamepo.exception;

/**
 * class for handling user exception,
 * launched when the user does not provide correct input
 */
public class UserException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private static final String message= "UserException";
    private String details;
    private String todo;

    public UserException() {
    }

    public UserException( String details, String todo) {
        this.details = details;
        this.todo = todo;
    }

    @Override
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
